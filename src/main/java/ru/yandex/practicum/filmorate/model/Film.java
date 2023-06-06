package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.web.valid.DateValid;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@Data
public class Film {
    private int id;
    @NotBlank(message = "Не может быть пустым")
    private String name;
    @Size(max = 200, message = "Не может быть больше 200 символов в описании")
    private String description;
    @DateValid
    private LocalDate releaseDate;
    @Positive()
    private int duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return getDuration() == film.getDuration() && getName()
                .equals(film.getName()) && getDescription().equals(film.getDescription()) && getReleaseDate()
                .equals(film.getReleaseDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getReleaseDate(), getDuration());
    }
}

