package ru.practicum.explore.rest.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.model.event.EventOutput;
import ru.practicum.explore.model.event.EventSort;
import ru.practicum.explore.service.eventService.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventRestHandler {

    private final EventService eventService;

    @GetMapping
    public List<EventOutput> findEvents(@RequestParam(required = false) String text,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) Boolean paid,
                                        @RequestParam(required = false) LocalDateTime rangeStart,
                                        @RequestParam(required = false) LocalDateTime rangeEnd,
                                        @RequestParam(required = false) Boolean onlyAvailable,
                                        @RequestParam(required = false, defaultValue = "VIEWS") EventSort sort,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        return eventService.searchEvent(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public EventOutput getEventById(@PathVariable long eventId) {
        return eventService.getEventById(eventId);
    }
}
