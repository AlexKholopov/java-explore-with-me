package ru.practicum.explore.model.category;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
public class CategoryDto {
    @Null
    private long id;
    @NotNull
    @Size(max = 20)
    private String name;
}
