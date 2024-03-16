package ru.practicum.explore.model.event;

import lombok.Data;
import ru.practicum.explore.utilits.EventDate;

import java.time.LocalDateTime;

@Data
public class EventCreateDto {

    private String annotation;
    private long category;
    private String description;
    @EventDate
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String title;
}
