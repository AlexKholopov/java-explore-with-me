package ru.practicum.explore.service.requestService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.db.Dao;
import ru.practicum.explore.model.exceptions.ConflictException;
import ru.practicum.explore.model.exceptions.NotFoundException;
import ru.practicum.explore.model.request.Request;
import ru.practicum.explore.model.request.RequestOutput;
import ru.practicum.explore.model.request.RequestStatus;
import ru.practicum.explore.model.request.ConfirmeRequestsInput;
import ru.practicum.explore.service.userService.RequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestsService {
    private final Dao dao;
    private final RequestMapper requestMapper;

    public RequestOutput saveRequest(long userId, long eventId) {
        var event = dao.getEventById(eventId).orElseThrow(() -> new NotFoundException("No such event was found"));
        var requester = dao.getUserById(userId).orElseThrow(() -> new NotFoundException("No such user was found"));
        var request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(requester);
        request.setStatus(event.getParticipantLimit() == 0 ? RequestStatus.CONFIRMED : RequestStatus.PENDING);
        return requestMapper.toOutput(dao.saveRequest(request));
    }

    public RequestOutput cancelRequest(long userId, long requestId) {
        var request = dao.getRequestById(requestId).orElseThrow(() -> new NotFoundException("No such request was found"));
        if (request.getRequester().getId() != userId) {
            throw new ConflictException("You can cancel only your request");
        };
        dao.deleteRequest(request);
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

    public List<RequestOutput> updateRequestStatus(ConfirmeRequestsInput requestsInput) {
        var requests = dao.findRequestsById(requestsInput.getRequestIds());
        requests.forEach(it -> {
            it.setStatus(requestsInput.getStatus());
            dao.saveRequest(it);
        });
        return requests.stream().map(requestMapper::toOutput).collect(Collectors.toList());
    }
}
