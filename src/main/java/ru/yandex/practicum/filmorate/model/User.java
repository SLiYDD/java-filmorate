package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;


@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"email", "login"})
public class User {

    private int id;
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Введите валидный email адрес")
    private String email;
    @NotEmpty(message = "Не может быть пустым")
    @Pattern(regexp = "^\\S*$",
            message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private Set<Integer> friends;
}
