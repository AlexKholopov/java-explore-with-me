package ru.practicum.explore.model.compilation;

import lombok.Data;

import java.util.List;

@Data
public class CompilationInput {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
