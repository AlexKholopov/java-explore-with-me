package ru.practicum.explore.service.eventService;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventCreateDto;
import ru.practicum.explore.model.event.EventOutput;
import ru.practicum.explore.model.user.User;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface EventMapper {
    @Mapping(target = "createdOn", source = "createdOn")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "initiator", source = "initiator")
    Event fromInput(EventCreateDto eventInput, Category category, User initiator, LocalDateTime createdOn);

    EventOutput toOutput(Event event);
}
