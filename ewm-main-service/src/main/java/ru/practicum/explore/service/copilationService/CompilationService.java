package ru.practicum.explore.service.copilationService;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.db.Dao;
import ru.practicum.explore.model.compilation.CompilationInput;
import ru.practicum.explore.model.compilation.CompilationOutput;
import ru.practicum.explore.model.event.Location;
import ru.practicum.explore.model.exceptions.NotFoundException;
import ru.practicum.explore.service.eventService.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {

    private final Dao dao;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final Gson gson = new Gson();


    public CompilationOutput addCompilation(CompilationInput compilationInput) {
        var events = dao.getEventsByIds(compilationInput.getEvents());
        var compilation = compilationMapper.fromInput(compilationInput, events);
        var savedCompilation = dao.saveCompilation(compilation);
        var compilationOutput = new CompilationOutput();
        compilationOutput.setEvents(savedCompilation.getEvents().stream().map(event -> {
            Location location = gson.fromJson(event.getLocation(), Location.class);
            return eventMapper.toOutput(event, location);
        }).collect(Collectors.toList()));
        compilationOutput.setTitle(compilationOutput.getTitle());
        compilationOutput.setTitle(compilation.getTitle());
        return compilationOutput;
    }

    public void deleteCompilation(Long compilationId) {
        var compilation = dao.getCompilationById(compilationId).orElseThrow(() -> new NotFoundException("No such compilation was found"));
        dao.deleteCompilation(compilation);
    }

    public CompilationOutput updateCompilation(Long compilationId, CompilationInput compilationInput) {
        var compilation = dao.getCompilationById(compilationId).orElseThrow(() -> new NotFoundException("No such compilation was found"));
        var events = dao.getEventsByIds(compilationInput.getEvents());
        var updatedCompilation = compilationMapper.fromInput(compilationInput, events);
        updatedCompilation.setId(compilationId);
        var savedCompilation = dao.saveCompilation(updatedCompilation);
        var compilationOutput = new CompilationOutput();
        compilationOutput.setEvents(savedCompilation.getEvents().stream().map(event -> {
            Location location = gson.fromJson(event.getLocation(), Location.class);
            return eventMapper.toOutput(event, location);
        }).collect(Collectors.toList()));
        compilationOutput.setTitle(compilationOutput.getTitle());
        compilationOutput.setTitle(compilation.getTitle());
        return compilationOutput;
    }

    public List<CompilationOutput> getCompilations(Boolean pinned, int from, int size) {
        return dao.findCompilations(pinned, from, size).stream().map(it -> {
            var eventsOut = it.getEvents().stream().map(event -> {
                Location location = gson.fromJson(event.getLocation(), Location.class);
                return eventMapper.toOutput(event, location);
            }).collect(Collectors.toList());
            return compilationMapper.toOutput(it, eventsOut);
        }).collect(Collectors.toList());
    }
}
