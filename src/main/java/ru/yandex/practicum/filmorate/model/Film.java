package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.web.valid.DateValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;


@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"name", "description", "releaseDate"})
public class Film {
    private int id;
    @NotBlank(message = "Не может быть пустым")
    private String name;
    @Size(max = 200, message = "Не может быть больше 200 символов в описании")
    private String description;
    @DateValid
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательной")
    private int duration;
    private Set<Integer> likes;
}

