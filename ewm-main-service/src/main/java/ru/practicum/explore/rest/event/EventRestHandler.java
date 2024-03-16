package ru.practicum.explore.rest.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.model.event.EventOutput;
import ru.practicum.explore.service.eventService.EventService;

import java.util.List;

@RestController("/events")
@RequiredArgsConstructor
public class EventRestHandler {

    private final EventService eventService;

    @GetMapping
    public List<EventOutput> findEvents() {

    }
}
