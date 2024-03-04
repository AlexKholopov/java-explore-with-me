package ru.practicum.explore.model.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @Null
    private Long id;
    @NotNull
    @Size(max = 20)
    private String name;
    @Email
    @NotNull
    private String email;
}
