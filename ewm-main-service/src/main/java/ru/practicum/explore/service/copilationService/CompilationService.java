package ru.practicum.explore.service.copilationService;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.explore.db.Dao;
import ru.practicum.explore.model.compilation.CompilationInputCreate;
import ru.practicum.explore.model.compilation.CompilationInputUpdate;
import ru.practicum.explore.model.compilation.CompilationOutput;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.Location;
import ru.practicum.explore.model.exceptions.NotFoundException;
import ru.practicum.explore.service.eventService.EventMapper;
import ru.practicum.model.HitOutput;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PropertySource(value = "application.properties")
public class CompilationService {

    private final Dao dao;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    @Value("${server.url}")
    private String serverUrl;
    private StatsClient statsClient;
    private final Gson gson = new Gson();
    private final static LocalDateTime earliestTime = LocalDateTime.now().minusYears(500);
    private final static LocalDateTime latestTime = LocalDateTime.now().plusYears(500);

    @PostConstruct
    private void initStatsClient() {
        statsClient= new StatsClient(serverUrl);
    }


    public CompilationOutput addCompilation(CompilationInputCreate compilationInputCreate) {
        var events = compilationInputCreate.getEvents() == null ? null : dao.getEventsByIds(compilationInputCreate.getEvents());
        if (compilationInputCreate.getPinned() == null) compilationInputCreate.setPinned(false);
        var compilation = compilationMapper.fromInput(compilationInputCreate, events);
        var savedCompilation = dao.saveCompilation(compilation);
        var uris = events == null ? new ArrayList<String>() : events.stream().map(it -> String.format("/events/%s", it.getId())).collect(Collectors.toList());
        var stats = statsClient.getHits(earliestTime, latestTime, uris, true).stream()
                .collect(Collectors.toMap((HitOutput it) -> it.getUri().substring(it.getUri().lastIndexOf("/") + 1), (HitOutput::getHits)));
        var eventsOut = savedCompilation.getEvents() == null ? null : savedCompilation.getEvents().stream().map(event -> {
            Location location = gson.fromJson(event.getLocation(), Location.class);
            return eventMapper.toOutput(event, location, dao.getConfirmedRequests(event), stats.getOrDefault(String.format("/events/%s", event.getId()), 0));
        }).collect(Collectors.toList());
        return compilationMapper.toOutput(savedCompilation, eventsOut);
    }

    public void deleteCompilation(Long compilationId) {
        var compilation = dao.getCompilationById(compilationId).orElseThrow(() -> new NotFoundException("No such compilation was found"));
        dao.deleteCompilation(compilation);
    }

    public CompilationOutput updateCompilation(Long compilationId, CompilationInputUpdate compilationInputUpdate) {
        var compilation = dao.getCompilationById(compilationId).orElseThrow(() -> new NotFoundException("No such compilation was found"));
        var events = compilationInputUpdate.getEvents() == null ? null : dao.getEventsByIds(compilationInputUpdate.getEvents());
        var updatedCompilation = compilationMapper.fromInput(compilationInputUpdate, events);
        updatedCompilation.setId(compilationId);
        var savedCompilation = dao.saveCompilation(updatedCompilation);
        var uris = events == null ? new ArrayList<String>() : events.stream().map(it -> String.format("/events/%s", it.getId())).collect(Collectors.toList());
        var stats = statsClient.getHits(earliestTime, latestTime, uris, true).stream()
                .collect(Collectors.toMap((HitOutput it) -> it.getUri().substring(it.getUri().lastIndexOf("/") + 1), (HitOutput::getHits)));
        var eventsOut = savedCompilation.getEvents() == null ? null : savedCompilation.getEvents().stream().map(event -> {
            Location location = gson.fromJson(event.getLocation(), Location.class);
            return eventMapper.toOutput(event, location, dao.getConfirmedRequests(event), stats.getOrDefault(String.format("/events/%s", event.getId()), 0));
        }).collect(Collectors.toList());
        return compilationMapper.toOutput(savedCompilation, eventsOut);
    }

    public List<CompilationOutput> getCompilations(Boolean pinned, int from, int size) {
        var compilations = dao.findCompilations(pinned, from, size);
        List<String> uris = new ArrayList<>();
        for (var comp : compilations) {
            uris.addAll(comp.getEvents().stream().map(it -> String.format("/events/%s", it.getId())).collect(Collectors.toList()));
        }
        var stats = statsClient.getHits(earliestTime, latestTime, uris, true).stream()
                .collect(Collectors.toMap((HitOutput it) -> it.getUri().substring(it.getUri().lastIndexOf("/") + 1), (HitOutput::getHits)));
        return compilations.stream().map(it -> {
            var eventsOut = it.getEvents().stream().map((Event event) -> {
                Location location = gson.fromJson(event.getLocation(), Location.class);
                return eventMapper.toOutput(event, location, dao.getConfirmedRequests(event), stats.getOrDefault(String.format("/events/%s", event.getId()), 0));
            }).collect(Collectors.toList());
            return compilationMapper.toOutput(it, eventsOut);
        }).collect(Collectors.toList());
    }

    public CompilationOutput getCompilation(Long compId) {
        var comp = dao.getCompilationById(compId).orElseThrow(() -> new NotFoundException("No such compilation was found"));
        var uris = comp.getEvents().stream().map(it -> String.format("/events/%s", it.getId())).collect(Collectors.toList());
        var stats = statsClient.getHits(earliestTime, latestTime, uris, true).stream()
                .collect(Collectors.toMap((HitOutput it) -> it.getUri().substring(it.getUri().lastIndexOf("/") + 1), (HitOutput::getHits)));
        var eventsOut = comp.getEvents().stream().map((Event event) -> {
            Location location = gson.fromJson(event.getLocation(), Location.class);
            return eventMapper.toOutput(event, location, dao.getConfirmedRequests(event), stats.getOrDefault(String.format("/events/%s", event.getId()), 0));
        }).collect(Collectors.toList());
        return compilationMapper.toOutput(comp, eventsOut);
    }
}
