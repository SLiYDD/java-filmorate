package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO FILMS(NAME, DESCRIPTION, DURATION, RELEASE, MPA_ID)" +
                "VALUES(:name, :description, :duration, :release, :mpaId)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("name", film.getName());
        map.addValue("description", film.getDescription());
        map.addValue("duration", film.getDuration());
        map.addValue("release", film.getReleaseDate());
        map.addValue("mpaId", film.getMpa().getId());
        jdbcOperations.update(sql, map, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE FILMS SET NAME = :name" +
                ", DESCRIPTION = :description" +
                ", DURATION = :duration" +
                ", RELEASE = :release" +
                ", MPA_ID = :mpaId" +
                "  where FILM_ID = :filmId";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("name", film.getName());
        map.addValue("description", film.getDescription());
        map.addValue("duration", film.getDuration());
        map.addValue("release", film.getReleaseDate());
        map.addValue("filmId", film.getId());
        map.addValue("mpaId", film.getMpa().getId());
        jdbcOperations.update(sql, map);
        return film;

    }

    @Override
    public List<Film> findAllFilms() {
        String sql = "SELECT FILMS.FILM_ID" +
                ", FILMS.NAME" +
                ", DESCRIPTION" +
                ", DURATION" +
                ", RELEASE" +
                ", FILMS.MPA_ID" +
                ", M.NAME FROM FILMS" +
                "  LEFT JOIN MPA M on M.MPA_ID = FILMS.MPA_ID";
        return jdbcOperations.query(sql, new FilmRowMapper());
    }

    @Override
    public Optional<Film> findFilmById(Integer filmId) {
        String sql = "SELECT FILMS.FILM_ID" +
                ", FILMS.NAME" +
                ", DESCRIPTION" +
                ", DURATION" +
                ", RELEASE" +
                ", FILMS.MPA_ID" +
                ", M.NAME FROM FILMS" +
                "  LEFT JOIN MPA M on M.MPA_ID = FILMS.MPA_ID where FILM_ID = :filmId";
        MapSqlParameterSource map = new MapSqlParameterSource(Map.of("filmId", filmId));
        final List<Film> films = jdbcOperations.query(sql, map, new FilmRowMapper());
        if (films.size() == 1) {
            var film = films.get(0);
            return Optional.of(film);
        }
        return Optional.empty();
    }

    public boolean filmExist(int filmId) {
        String sql = "SELECT FILM_ID FROM FILMS WHERE FILM_ID = :filmId";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("filmId", filmId);
        List<Integer> film = jdbcOperations.query(sql, map, ((rs, rowNum) -> rs.getInt("FILM_ID")));
        return !film.isEmpty();
    }

    @Override
    public void deleteFilm(int filmId) {
        String sql = "DELETE FROM FILMS WHERE FILM_ID = :userId";
        var map = new MapSqlParameterSource();
        map.addValue("filmId", filmId);
        jdbcOperations.update(sql, map);
    }
}
