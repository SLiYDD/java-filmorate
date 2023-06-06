package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Objects;

@Data
public class User {
    private int id;
    @Email(message = "Введите валидный email адрес")
    private String email;
    @NotEmpty(message = "Не может быть пустым")
    @Pattern(regexp = "^\\S*$",
             message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getEmail().equals(user.getEmail()) && getLogin().equals(user.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getLogin());
    }
}
