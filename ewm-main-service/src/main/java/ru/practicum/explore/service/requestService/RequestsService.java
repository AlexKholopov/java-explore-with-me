package ru.practicum.explore.service.requestService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.db.Dao;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.exceptions.ConflictException;
import ru.practicum.explore.model.exceptions.NotFoundException;
import ru.practicum.explore.model.request.ConfirmeRequestsInput;
import ru.practicum.explore.model.request.GroupedRequestsOutput;
import ru.practicum.explore.model.request.Request;
import ru.practicum.explore.model.request.RequestOutput;
import ru.practicum.explore.model.request.RequestStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestsService {
    private final Dao dao;
    private final RequestMapper requestMapper;

    public RequestOutput saveRequest(long userId, long eventId) {
        var event = dao.getEventById(eventId).orElseThrow(() -> new NotFoundException("No such event was found"));
        if (event.getState() != EventState.PUBLISHED) throw new ConflictException("Event not published yet");
        var requester = dao.getUserById(userId).orElseThrow(() -> new NotFoundException("No such user was found"));
        var oldRequest = dao.getRequestByRequesterAndEvent(requester, event);
        if (event.getInitiator().equals(requester)) throw new ConflictException("You can't request to you own event");
        if (oldRequest != null) throw new ConflictException("You already have one request");
        long confirmedRequests = dao.getConfirmedRequests(event);
        var request = new Request();
        request.setCreated(LocalDateTime.now().withNano(0));
        request.setEvent(event);
        request.setRequester(requester);
        request.setStatus(calcStatus(event, confirmedRequests));
        return requestMapper.toOutput(dao.saveRequest(request));
    }

    public RequestOutput cancelRequest(long userId, long requestId) {
        var request = dao.getRequestById(requestId).orElseThrow(() -> new NotFoundException("No such request was found"));
        if (request.getRequester().getId() != userId) {
            throw new ConflictException("You can cancel only your request");
        }
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toOutput(request);
    }

    public List<RequestOutput> getUserRequests(long userId) {
        var requester = dao.getUserById(userId).orElseThrow(() -> new NotFoundException("No such user was found"));
        return dao.getUserRequests(requester).stream().map(requestMapper::toOutput).collect(Collectors.toList());
    }

    public List<RequestOutput> getUserEventRequests(long userId, long eventId) {
        var requester = dao.getUserById(userId).orElseThrow(() -> new NotFoundException("No such user was found"));
        var event = dao.getEventById(eventId).orElseThrow(() -> new NotFoundException("No such event was found"));
        if (!event.getInitiator().equals(requester)) {
            throw new ConflictException("You have no right to see requesters");
        }

        return dao.getUserEventRequests(event).stream().map(requestMapper::toOutput).collect(Collectors.toList());
    }

    public GroupedRequestsOutput updateRequestStatus(long eventId, ConfirmeRequestsInput requestsInput) {
        var requests = dao.findRequestsById(requestsInput.getRequestIds());
        var event = dao.getEventById(eventId).orElseThrow(() -> new NotFoundException("No such event was found"));
        var participant = dao.getConfirmedRequests(event);
        if (participant >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ConflictException("Participant limit was reached");
        }
        requests.forEach(it -> {
            switch (requestsInput.getStatus()) {
                case CONFIRMED:

                    if (participant < event.getParticipantLimit() || event.getParticipantLimit() == 0) {
                        it.setStatus(requestsInput.getStatus());
                        dao.saveRequest(it);
                    } else {
                        break;
                    }
                    break;
                case REJECTED:
                    it.setStatus(requestsInput.getStatus());
                    dao.saveRequest(it);
            }

        });
        var res = new GroupedRequestsOutput();
        var groupedRequests = dao.getUserEventRequests(event).stream().map(requestMapper::toOutput).collect(Collectors.groupingBy(RequestOutput::getStatus));
        res.setConfirmedRequests(groupedRequests.get(RequestStatus.CONFIRMED));
        res.setRejectedRequests(groupedRequests.get(RequestStatus.REJECTED));
        return res;
    }

    private RequestStatus calcStatus(Event event, long confirmedRequests) {
        if (event.getParticipantLimit() == 0 || (!event.isRequestModeration() && event.getParticipantLimit() > confirmedRequests))
            return RequestStatus.CONFIRMED;
        else if (event.getParticipantLimit() <= confirmedRequests)
            throw new ConflictException("Participant limit was reached");
        else return RequestStatus.PENDING;
    }
}
