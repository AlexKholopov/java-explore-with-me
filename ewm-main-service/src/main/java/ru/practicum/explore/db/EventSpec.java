package ru.practicum.explore.db;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.event.Event;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class EventSpec {
    public static final int SEARCH_MIN_LENGTH = 2;

    public static Specification<Event> searchByText(String search) {
        if (!StringUtils.hasLength(search)) {
            return null;
        }
        return Specification.where(like(search, "annotation"))
                .or(like(search, "description"));
    }

    public static Specification<Event> CategoryIn(List<Category> searchExpressions) {
        return ((root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Category> inClause = criteriaBuilder.in(root.get("category"));
            for (var searchExpression : searchExpressions) {
                inClause.value(searchExpression);
            }
            return inClause;
        });
    }

    public static Specification<Event> isPaid(Boolean paid) {
        if (Objects.isNull(paid)) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), paid));
    }

    public static Specification<Event> isAvailable(Boolean available) {
        if (Objects.isNull(available)) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("available")));
    }

    public static Specification<Event> rangeStart(LocalDateTime start) {
        if (Objects.isNull(start)) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), start));
    }

    public static Specification<Event> rangeEnd(LocalDateTime end) {
        if (Objects.isNull(end)) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end));
    }

    private static Specification<Event> like(String searchExpression, String searchField) {
        String expression = toLikeExpression(searchExpression);
        if (Objects.isNull(expression)) {
            return null;
        }
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get(searchField)), expression));
    }

    public static String toLikeExpression(String query) {
        if (!StringUtils.hasText(query)) {
            return null;
        }

        String modified = query.trim().replaceAll(" ", "").toLowerCase();
        if (modified.length() < SEARCH_MIN_LENGTH) {
            return null;
        }
        return '%' + modified + '%';
    }
}
