package ru.practicum.explore.service.categoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.explore.db.Dao;
import ru.practicum.explore.model.category.CategoryDto;
import ru.practicum.explore.model.exceptions.ConflictException;
import ru.practicum.explore.model.exceptions.NotFoundException;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final Dao dao;
    private final CategoryMapper categoryMapper;

    public CategoryDto createCategory(CategoryDto categoryDto) {
        try {
            return categoryMapper.toDto(dao.saveCategory(categoryMapper.fromDto(categoryDto)));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Category name is already used");
        }
    }

    public void deleteCategory(long id) {
        try {
            dao.deleteCategory(id);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Category is using");
        }

    }

    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        var oldCat = dao.getCategoryById(catId);
        if (oldCat != null) {
            try {
                var cat = dao.saveCategory(categoryMapper.fromDto(categoryDto));
                return categoryMapper.toDto(cat);
            } catch (DataIntegrityViolationException e) {
                if (oldCat.getName().equals(categoryDto.getName())) {
                    return categoryDto;
                }
                throw new ConflictException("Category name is already used");
            }
        } else {
            throw new ValidationException("No such category was found");
        }
    }

    public List<CategoryDto> getCategories(int from, int size) {
        return dao.getAllCategoriesPage(from, size).stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(long id) {
        var cat = dao.getCategoryById(id);
        if (cat == null) throw new NotFoundException("No such category was found");
        return categoryMapper.toDto(cat);
    }


}
