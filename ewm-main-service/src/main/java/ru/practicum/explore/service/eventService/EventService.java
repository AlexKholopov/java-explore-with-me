package ru.practicum.explore.service.eventService;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.db.Dao;
import ru.practicum.explore.model.event.EventCreateDto;
import ru.practicum.explore.model.event.EventOutput;
import ru.practicum.explore.model.event.EventSort;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.event.EventStateAction;
import ru.practicum.explore.model.event.EventUpdateDto;
import ru.practicum.explore.model.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final Dao dao;
    private final EventMapper eventMapper;
    private final Gson gson = new Gson();

    public EventOutput createEvent(Long userId, EventCreateDto eventInput) {
        var category = dao.getCategoryBId(eventInput.getCategory());
        var initiator = dao.getUserById(userId).orElseThrow(() -> new NotFoundException("No such initiator was found"));
        var event = eventMapper.fromInput(eventInput, category, initiator, LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setStateAction(EventStateAction.SEND_TO_REVIEW);
        var savedEvent = dao.saveEvent(event);
        return eventMapper.toOutput(savedEvent);
    }

    public List<EventOutput> getUsersEvents(long userId, int from, int size) {
        var initiator = dao.getUserById(userId).orElseThrow(() -> new NotFoundException("No such initiator was found"));
        return dao.getEventsByUserPageble(initiator, from, size).stream().map(eventMapper::toOutput).collect(Collectors.toList());
    }

    public EventOutput getEventByIdAndUser(long userId, long eventId) {
        var initiator = dao.getUserById(userId).orElseThrow(() -> new NotFoundException("No such initiator was found"));
        return eventMapper.toOutput(dao.getEventByUserAndEventId(initiator, eventId));
    }

    public EventOutput updateEvent(long userId, long eventId, EventUpdateDto eventInput) {
        var initiator = dao.getUserById(userId).orElseThrow(() -> new NotFoundException("No such initiator was found"));
        var event = dao.getEventByUserAndEventId(initiator, eventId);
        if (event == null) {
            throw new NotFoundException("No such event was found");
        }
        if (eventInput.getEventDate() != null) {
            event.setEventDate(eventInput.getEventDate());
        }
        if (eventInput.getAnnotation() != null) {
            event.setAnnotation(eventInput.getAnnotation());
        }
        if (eventInput.getDescription() != null) {
            event.setDescription(eventInput.getDescription());
        }
        if (eventInput.getLocation() != null) {
            event.setLocation(eventInput.getLocation());
        }
        if (eventInput.getTitle() != null) {
            event.setTitle(eventInput.getTitle());
        }
        if (eventInput.getParticipantLimit() != null) {
            event.setParticipantLimit(eventInput.getParticipantLimit());
        }
        if (eventInput.getCategory() != null) {
            event.setCategory(dao.getCategoryBId(eventInput.getCategory()));
        }
        if (eventInput.getRequestModeration() != null) {
            event.setRequestModeration(eventInput.getRequestModeration());
        }
        if (eventInput.getPaid() != null) {
            event.setPaid(eventInput.getPaid());
        }
        if (eventInput.getStateAction() != null) {
            event.setStateAction(eventInput.getStateAction());
        }

        return eventMapper.toOutput(dao.saveEvent(event));
    }

    public List<EventOutput> searchEvent(String text, List<Long> categoriesIds, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                         Boolean onlyAvailable, EventSort sort, int from, int size) {
        var categories = dao.findCategoriesById(categoriesIds);
        var events = dao.searchManyFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size);
        switch (sort) {
            case EVENT_DATE:
                break;
            case VIEWS:
                break;
        }
        return events.stream().map(eventMapper::toOutput).collect(Collectors.toList());
    }
}
