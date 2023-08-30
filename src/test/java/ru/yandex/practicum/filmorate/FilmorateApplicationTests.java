package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {
    private final FilmDao filmDao;
    private final MpaDao mpaDao;
    private final UserDao userDao;

    @Test
    void getAllFilms() {
        Film film1 = Film.builder()
                .id(1)
                .name("ПОБЕГ ИЗ ШОУШЕНКА")
                .description("МОЛОДОГО ФИНАНСИСТА ЭНДИ ДЮФРЕЙНА ПОДОЗРЕВАЮТ В УБИЙСТВЕ, КОТОРОГО ОН НЕ СОВЕРШАЛ" +
                        ". НЕСМОТРЯ НА ЭТО ЕГО ПРИГОВАРИВАЮТ К ПОЖИЗНЕННОМУ ЗАКЛЮЧЕНИЮ В ТЮРЬМЕ, ИЗ КОТОРОЙ ЕЩЁ НИКОМУ " +
                        "НЕ УДАВАЛОСЬ СБЕЖАТЬ.")
                .releaseDate(LocalDate.now())
                .duration(200)
                .genres(new HashSet<>())
                .mpa(mpaDao.findMpaById(1).get())
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("КРЁСТНЫЙ ОТЕЦ")
                .description("ПЕРВАЯ ЧАСТЬ КРИМИНАЛЬНОЙ САГИ О СИЦИЛИЙСКОЙ МАФИОЗНОЙ СЕМЬЕ")
                .releaseDate(LocalDate.now())
                .duration(180)
                .genres(new HashSet<>())
                .mpa(mpaDao.findMpaById(1).get())
                .build();
        filmDao.addFilm(film1);
        filmDao.addFilm(film2);
        Collection<Film> films = filmDao.findAllFilms();

        assertThat(films).hasSize(2);
    }

    @Test
    void createFilm() {
        Film film = Film.builder()
                .id(2)
                .name("КРЁСТНЫЙ ОТЕЦ")
                .description("ПЕРВАЯ ЧАСТЬ КРИМИНАЛЬНОЙ САГИ О СИЦИЛИЙСКОЙ МАФИОЗНОЙ СЕМЬЕ")
                .releaseDate(LocalDate.now())
                .duration(180)
                .genres(new HashSet<>())
                .mpa(mpaDao.findMpaById(2).get())
                .build();
        filmDao.addFilm(film);
        Film filmOpt = filmDao.findFilmById(1).get();

        assertEquals(filmOpt.getId(), 1);
    }

    @Test
    void getFilmById() {
        Film film = Film.builder()
                .id(2)
                .name("КРЁСТНЫЙ ОТЕЦ")
                .description("ПЕРВАЯ ЧАСТЬ КРИМИНАЛЬНОЙ САГИ О СИЦИЛИЙСКОЙ МАФИОЗНОЙ СЕМЬЕ")
                .releaseDate(LocalDate.now())
                .duration(180)
                .genres(new HashSet<>())
                .mpa(mpaDao.findMpaById(1).get())
                .build();
        filmDao.addFilm(film);

        assertEquals(filmDao.findFilmById(1).get().getId(), film.getId());
    }

    @Test
    public void getAllUsers() {
        User user1 = User.builder()
                .id(1)
                .name("ФЕДОРОВА АЛИНА")
                .login("LOGIN1")
                .email("1@YA.RU")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        User user2 = User.builder()
                .id(2)
                .name("ЧЕРНОВА ЕКАТЕРИНА")
                .login("LOGIN2")
                .email("2@YA.RU")
                .birthday(LocalDate.of(1999, 7, 19))
                .build();
        userDao.addUser(user1);
        userDao.addUser(user2);
        List<User> users = userDao.findAllUsers();

        assertThat(users).contains(user1);
        assertThat(users).contains(user2);
    }

    @Test
    public void createUser() {
        User user = User.builder()
                .id(1)
                .name("ФЕДОРОВА АЛИНА")
                .login("LOGIN1")
                .email("1@YA.RU")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        userDao.addUser(user);
        User userOpt = userDao.findUserById(1).get();

        assertEquals(userOpt.getId(), 1);
    }
}