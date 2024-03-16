package ru.practicum.explore.db.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.user.User;

import java.util.List;

public interface EventRepo extends PagingAndSortingRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> findByInitiator(User initiator, Pageable pageable);

    Event findByInitiatorAndId(User initiator, Long id);

}
