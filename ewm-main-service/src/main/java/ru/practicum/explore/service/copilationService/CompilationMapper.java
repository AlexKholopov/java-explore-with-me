package ru.practicum.explore.service.copilationService;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.model.compilation.CompilationInput;
import ru.practicum.explore.model.compilation.CompilationOutput;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventOutput;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface CompilationMapper {
    @Mapping(target = "events", source = "events")
    Compilation fromInput(CompilationInput compilationInput, List<Event> events);

    @Mapping(target = "events", source = "events")
    CompilationOutput toOutput(Compilation compilation, List<EventOutput> events);
}
