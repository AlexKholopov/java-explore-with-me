package ru.practicum.explore.db;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.practicum.explore.db.repo.CategoryRepo;
import ru.practicum.explore.db.repo.UserRepo;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.user.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Dao {
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;

    public User createUser(User user) {
        return userRepo.save(user);
    }
    public void deleteUserById(long userId) {
        userRepo.deleteById(userId);
    }
    public List<User> getUsersByIdIn(List<Long> ids) {
        return userRepo.findByIdIn(ids);
    }
    public List<User> getAllUsersPage(int from, int size) {
        int page = from % size > 0 ? (from / size) + 1 : from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        return userRepo.findAll(pageRequest).toList();
    }

    public Category  saveCategory(Category category) {
        return categoryRepo.save(category);
    }

    public Category getCategoryBId(long id) {
        return categoryRepo.findById(id).orElse(null);
    }

    public void deleteCategory(long id) {
        categoryRepo.deleteById(id);
    }

    public List<Category> getAllCategoriesPage(int from, int size) {
        int page = from % size > 0 ? (from / size) + 1 : from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        return categoryRepo.findAll(pageRequest).toList();
    }
}
