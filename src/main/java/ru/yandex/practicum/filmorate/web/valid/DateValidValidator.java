package ru.yandex.practicum.filmorate.web.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class DateValidValidator implements ConstraintValidator<DateValid, LocalDate> {
    private static final LocalDate BIRTHCINEMA = LocalDate.of(1895, Month.DECEMBER, 28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(BIRTHCINEMA);
    }
}
