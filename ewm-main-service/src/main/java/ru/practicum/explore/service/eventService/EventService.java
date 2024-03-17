package ru.practicum.explore.service.eventService;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.explore.db.Dao;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventCreateDto;
import ru.practicum.explore.model.event.EventOutput;
import ru.practicum.explore.model.event.EventSort;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.event.EventStateAction;
import ru.practicum.explore.model.event.EventUpdateDto;
import ru.practicum.explore.model.event.Location;
import ru.practicum.explore.model.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final Dao dao;
    private final EventMapper eventMapper;
    private final Gson gson = new Gson();
    private final StatsClient statsClient = new StatsClient();

    public EventOutput createEvent(Long userId, EventCreateDto eventInput) {
        var category = dao.getCategoryBId(eventInput.getCategory());
        var initiator = dao.getUserById(userId).orElseThrow(() -> new NotFoundException("No such initiator was found"));
        var locationStr = gson.toJson(eventInput.getLocation());
        var event = eventMapper.fromInput(eventInput, category, initiator, LocalDateTime.now(), locationStr);
        event.setState(EventState.PENDING);
        event.setStateAction(EventStateAction.SEND_TO_REVIEW);
        var savedEvent = dao.saveEvent(event);
        Location location = gson.fromJson(savedEvent.getLocation(), Location.class);
        return eventMapper.toOutput(savedEvent, location);
    }

    public List<EventOutput> getUsersEvents(long userId, int from, int size) {
        var initiator = dao.getUserById(userId).orElseThrow(() -> new NotFoundException("No such initiator was found"));
        return dao.getEventsByUserPageble(initiator, from, size).stream().map(event -> {
            Location location = gson.fromJson(event.getLocation(), Location.class);
            return eventMapper.toOutput(event, location);
        }).collect(Collectors.toList());
    }

    public EventOutput getEventByIdAndUser(long userId, long eventId) {
        var initiator = dao.getUserById(userId).orElseThrow(() -> new NotFoundException("No such initiator was found"));
        var event = dao.getEventByUserAndEventId(initiator, eventId);
        Location location = gson.fromJson(event.getLocation(), Location.class);
        return eventMapper.toOutput(event, location);
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
            var location = gson.toJson(eventInput.getLocation());
            event.setLocation(location);
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

        var savedEvent = dao.saveEvent(event);
        Location location = gson.fromJson(savedEvent.getLocation(), Location.class);
        return eventMapper.toOutput(savedEvent, location);
    }

    public List<EventOutput> searchEvent(String text, List<Long> categoriesIds, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                         Boolean onlyAvailable, EventSort sort, int from, int size) {
        var categories = dao.findCategoriesById(categoriesIds);
        var events = dao.searchManyFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size);
        LocalDateTime earliestTime = rangeStart == null ? LocalDateTime.MAX : rangeStart;
        LocalDateTime latestTime = rangeEnd == null ? LocalDateTime.MIN : rangeEnd;
        List<EventOutput> result = new ArrayList<>();
        switch (sort) {
            case EVENT_DATE:
                result = events.stream().sorted(Comparator.comparing(Event::getEventDate)).map(event -> {
                    Location location = gson.fromJson(event.getLocation(), Location.class);
                    return eventMapper.toOutput(event, location);
                }).collect(Collectors.toList());
                break;
            case VIEWS:
                result = events.stream().map(event -> {
                    var stat = statsClient.getHits(earliestTime, latestTime, String.format("/events/%s", event.getId()), false);
                    Location location = gson.fromJson(event.getLocation(), Location.class);
                    var eventOut = eventMapper.toOutput(event, location);
                    try {
                        eventOut.setViews(stat.getBody().getHits());
                    } catch (NullPointerException e) {
                        eventOut.setViews(0);
                    }
                    return eventOut;
                }).collect(Collectors.toList());
                break;
        }
        return result;
    }

    public EventOutput getEventById(long eventId) {
        return dao.getEventById(eventId).map(event -> {
            Location location = gson.fromJson(event.getLocation(), Location.class);
            return eventMapper.toOutput(event, location);
        }).orElseThrow(() -> new NotFoundException("No such event was found"));
    }
}
