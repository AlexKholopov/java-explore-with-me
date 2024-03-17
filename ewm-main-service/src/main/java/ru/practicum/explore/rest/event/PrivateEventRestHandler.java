package ru.practicum.explore.rest.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.model.event.EventCreateDto;
import ru.practicum.explore.model.event.EventOutput;
import ru.practicum.explore.model.event.EventUpdateDto;
import ru.practicum.explore.model.request.ConfirmeRequestsInput;
import ru.practicum.explore.model.request.RequestOutput;
import ru.practicum.explore.service.eventService.EventService;
import ru.practicum.explore.service.requestService.RequestsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventRestHandler {

    private final EventService eventService;
    private final RequestsService requestsService;

    @GetMapping
    public List<EventOutput> getUserMadeEvents(@PathVariable long userId,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        return eventService.getUsersEvents(userId, from, size);
    }

    @PostMapping
    public EventOutput createEvent(@PathVariable long userId, @Valid @RequestBody EventCreateDto eventCreateDto) {
        return eventService.createEvent(userId, eventCreateDto);
    }

    @GetMapping("/{eventId}")
    public EventOutput getUserEvent(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.getEventByIdAndUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventOutput updateEvent(@PathVariable long userId, @PathVariable long eventId, @Valid @RequestBody EventUpdateDto eventUpdateDto) {
        return eventService.updateEvent(userId, eventId, eventUpdateDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestOutput> getUserEventRequests(@PathVariable long userId, @PathVariable long eventId) {
        return requestsService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public List<RequestOutput> confirmRequest(@Valid @RequestBody ConfirmeRequestsInput requestsInput) {
        return requestsService.updateRequestStatus(requestsInput);
    }
}
