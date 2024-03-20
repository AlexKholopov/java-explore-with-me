package ru.practicum.explore.db;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.explore.db.repo.CategoryRepo;
import ru.practicum.explore.db.repo.CompilationRepo;
import ru.practicum.explore.db.repo.EventRepo;
import ru.practicum.explore.db.repo.RequestRepo;
import ru.practicum.explore.db.repo.UserRepo;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.request.Request;
import ru.practicum.explore.model.request.RequestStatus;
import ru.practicum.explore.model.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Dao {
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final EventRepo eventRepo;
    private final RequestRepo requestRepo;
    private final CompilationRepo compilationRepo;

    public User createUser(User user) {
        return userRepo.save(user);
    }

    public void deleteUserById(long userId) {
        userRepo.deleteById(userId);
    }

    public List<User> getUsersByIdIn(List<Long> ids) {
        return userRepo.findByIdIn(ids);
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public List<User> getAllUsersPage(int from, int size) {
        int page = from % size > 0 ? (from / size) + 1 : from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        return userRepo.findAll(pageRequest).toList();
    }

    public Category saveCategory(Category category) {
        return categoryRepo.save(category);
    }

    public Category getCategoryById(long id) {
        return categoryRepo.findById(id).orElse(null);
    }

    public List<Category> findCategoriesById(List<Long> ids) {
        return categoryRepo.findByIdIn(ids);
    }

    public void deleteCategory(long id) {
        categoryRepo.deleteById(id);
    }

    public List<Category> getAllCategoriesPage(int from, int size) {
        int page = from % size > 0 ? (from / size) + 1 : from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        return categoryRepo.findAll(pageRequest).toList();
    }

    public Event saveEvent(Event event) {
        return eventRepo.save(event);
    }

    public List<Event> getEventsByUserPageble(User user, int from, int size) {
        int page = from % size > 0 ? (from / size) + 1 : from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        return eventRepo.findByInitiator(user, pageRequest);
    }

    public List<Event> getEventsByIds(List<Long> ids) {

        return eventRepo.findByIdIn(ids);
    }

    public Event getEventByUserAndEventId(User user, long eventId) {
        return eventRepo.findByInitiatorAndId(user, eventId);
    }

    public Optional<Event> getEventById(long eventId) {
        return eventRepo.findById(eventId);
    }

    public Event getPublishedEventById(long eventId) {
        return eventRepo.findByIdAndState(eventId, EventState.PUBLISHED);
    }

    public Request saveRequest(Request request) {
        return requestRepo.save(request);
    }

    public Optional<Request> getRequestById(long requestId) {
        return requestRepo.findById(requestId);
    }

    public Request getRequestByRequesterAndEvent(User requester, Event event) {
        return requestRepo.findByRequesterAndEvent(requester, event);
    }

    public void deleteRequest(Request request) {
        requestRepo.delete(request);
    }

    public List<Request> getUserRequests(User user) {
        return requestRepo.findByRequester(user);
    }

    public List<Request> getUserEventRequests(Event event) {
        return requestRepo.findByEvent(event);
    }

    public long getConfirmedRequests(Event event) {
        return requestRepo.countByEventAndStatus(event, RequestStatus.CONFIRMED);
    }

    public List<Request> findRequestsById(List<Long> requestsId) {
        return requestRepo.findByIdIn(requestsId);
    }

    public List<Event> searchManyFilters(String text, List<Category> categories, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                         Boolean onlyAvailable, int from, int size) {
        Specification<Event> specification = Specification
                .where(EventSpec.searchByText(text))
                .and(EventSpec.categoryIn(categories))
                .and(EventSpec.isPaid(paid))
                .and(EventSpec.rangeStart(rangeStart))
                .and(EventSpec.rangeEnd(rangeEnd))
                .and(EventSpec.isAvailable(onlyAvailable));

        int page = from % size > 0 ? (from / size) + 1 : from / size;
        PageRequest pageRequest = PageRequest.of(page, size);

        return eventRepo.findAll(specification, pageRequest).toList();
    }

    public List<Event> searchManyFiltersAdmin(List<User> users, List<EventState> states, List<Category> categories,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        Specification<Event> specification = Specification
                .where(EventSpec.categoryIn(categories))
                .and(EventSpec.userIn(users))
                .and(EventSpec.stateIn(states))
                .and(EventSpec.rangeStart(rangeStart))
                .and(EventSpec.rangeEnd(rangeEnd));

        int page = from % size > 0 ? (from / size) + 1 : from / size;
        PageRequest pageRequest = PageRequest.of(page, size);

        return eventRepo.findAll(specification, pageRequest).toList();
    }

    public Compilation saveCompilation(Compilation compilation) {
        return compilationRepo.save(compilation);
    }

    public void deleteCompilation(Compilation compilation) {
        compilationRepo.delete(compilation);
    }

    public Optional<Compilation> getCompilationById(Long id) {
        return compilationRepo.findById(id);
    }

    public List<Compilation> findCompilations(Boolean pinned, int from, int size) {
        Specification<Compilation> specification = Specification
                .where(CompilationSpec.isPinned(pinned));

        int page = from % size > 0 ? (from / size) + 1 : from / size;
        PageRequest pageRequest = PageRequest.of(page, size);

        return compilationRepo.findAll(specification, pageRequest).toList();
    }
}
