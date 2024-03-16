package ru.practicum.explore.model.event;

import lombok.Data;
import ru.practicum.explore.model.category.CategoryDto;
import ru.practicum.explore.model.user.UserOutput;

import java.time.LocalDateTime;

@Data
public class EventOutput {
    private long id;
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private UserOutput initiator;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private String state;
    private String title;
    private int views;

}
