package ru.practicum.explore.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.model.category.CategoryDto;
import ru.practicum.explore.model.compilation.CompilationInput;
import ru.practicum.explore.model.compilation.CompilationOutput;
import ru.practicum.explore.model.user.UserDto;
import ru.practicum.explore.service.categoryService.CategoryService;
import ru.practicum.explore.service.copilationService.CompilationService;
import ru.practicum.explore.service.userService.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminRestHandler {
    private final UserService userService;
    private final CategoryService categoryService;
    private final CompilationService compilationService;

    @PostMapping("/users")
    public UserDto createUser (@RequestBody @Valid UserDto userDto) {
        return userService.createUser(userDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false, defaultValue = "") List<Long> ids,
                         @RequestParam(required = false, defaultValue = "0") int from,
                         @RequestParam(required = false, defaultValue = "10") int size) {
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/categories")
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping("/compilations")
    public CompilationOutput createCompilation(@RequestBody CompilationInput compilationInput) {
        return compilationService.addCompilation(compilationInput);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationOutput updateCompilation(@PathVariable long compId, @Valid @RequestBody CompilationInput compilationInput) {
        return compilationService.updateCompilation(compId, compilationInput);
    }
}
