package ru.practicum.service;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.db.model.DbHitData;
import ru.practicum.model.HitInput;

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface HitsMapper {
    DbHitData fromInput(HitInput hitInput);
}
