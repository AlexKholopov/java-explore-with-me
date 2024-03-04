package ru.practicum.explore.db.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.explore.model.category.Category;

public interface CategoryRepo extends PagingAndSortingRepository<Category, Long> {
}
