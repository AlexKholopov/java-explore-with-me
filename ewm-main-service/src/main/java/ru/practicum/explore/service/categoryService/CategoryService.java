package ru.practicum.explore.service.categoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.db.Dao;
import ru.practicum.explore.model.category.CategoryDto;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final Dao dao;
    private final CategoryMapper categoryMapper;

    public CategoryDto createCategory(CategoryDto categoryDto) {
        return categoryMapper.toDto(dao.saveCategory(categoryMapper.fromDto(categoryDto)));
    }

    public void deleteCategory(long id) {
        dao.deleteCategory(id);
    }

    public CategoryDto updateCategory(CategoryDto categoryDto) {
       if (dao.getCategoryBId(categoryDto.getId()) != null) {
            return categoryMapper.toDto(dao.saveCategory(categoryMapper.fromDto(categoryDto)));
        } else {
           throw new ValidationException("No such category was found");
       }
    }

    public List<CategoryDto> getCategories(int from, int size) {
        return dao.getAllCategoriesPage(from, size).stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(long id) {
        return categoryMapper.toDto(dao.getCategoryBId(id));
    }


}
