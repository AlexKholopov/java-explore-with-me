package ru.practicum.explore.model.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventUpdateDto {

    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
    private String title;

}
