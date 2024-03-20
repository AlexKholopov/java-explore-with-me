package ru.practicum.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HitOutput {
    private String app;
    private String uri;
    private Integer hits;
}
