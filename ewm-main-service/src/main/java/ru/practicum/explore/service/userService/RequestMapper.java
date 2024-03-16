package ru.practicum.explore.service.userService;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.explore.model.request.Request;
import ru.practicum.explore.model.request.RequestOutput;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface RequestMapper {
    RequestOutput toOutput(Request request);
}
