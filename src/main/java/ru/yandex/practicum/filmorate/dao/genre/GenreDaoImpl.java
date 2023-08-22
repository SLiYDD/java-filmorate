package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenres;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT * FROM GENRES";
        return jdbcOperations.query(sql,
                (rs, rowNum) -> Genre.builder()
                        .id(rs.getInt("GENRE_ID"))
                        .name(rs.getString("GENRE_NAME"))
                        .build());
    }

    @Override
    public List<Genre> findGenreByFilmId(int filmId) {
        String sql = "SELECT G.GENRE_ID, GENRE_NAME " +
                "FROM GENRES G " +
                "JOIN FILM_GENRES FG on G.GENRE_ID = FG.GENRE_ID " +
                "WHERE FILM_ID = :filmId ";
        return jdbcOperations.query(sql, new MapSqlParameterSource(Map.of("filmId", filmId)), (rs, rowNum) -> Genre.builder()
                .id(rs.getInt("GENRE_ID"))
                .name(rs.getString("GENRE_NAME"))
                .build());
    }

    @Override
    public Optional<Genre> findById(int genreId) {
        String sql = "select * from GENRES where genre_id = :genreId";
        final List<Genre> genres = jdbcOperations.query(sql, new MapSqlParameterSource(Map.of("genreId", genreId)), (rs, rowNum) -> Genre.builder()
                .id(rs.getInt("GENRE_ID"))
                .name(rs.getString("GENRE_NAME"))
                .build());

        if (genres.size() == 1) {
            var genre = genres.get(0);
            return Optional.of(genre);
        }
        return Optional.empty();
    }

    @Override
    public List<Genre> findByIds(List<Integer> genreIds) {
        String sql = "select GENRE_ID, GENRE_NAME from GENRES where GENRE_ID in (:genreIds)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("genreIds", genreIds);

        return jdbcOperations.query(sql, map, (rs, rowNum) -> Genre.builder()
                .id(rs.getInt("GENRE_ID"))
                .name(rs.getString("GENRE_NAME"))
                .build());
    }

    @Override
    public void deleteAllByFilmId(int filmId) {
        final String sql = "DELETE FROM FILM_GENRES where FILM_ID = :filmId";
        jdbcOperations.update(sql, new MapSqlParameterSource(Map.of("filmId", filmId)));
    }

    @Override
    public Optional<Genre> create(Genre genre) {
        String sql = "insert into GENRES (GENRE_NAME) values (:genreName)";
        jdbcOperations.update(sql, new MapSqlParameterSource(Map.of("genreName", genre.getName())));
        return Optional.of(genre);
    }

    @Override
    public Optional<Genre> update(Genre genre) {
        String sql = "update GENRES set GENRE_NAME = :genreName where genre_id = :genreId";
        jdbcOperations.update(sql, new MapSqlParameterSource(Map.of("genreName", genre.getName(), "genreId", genre.getId())));
        return Optional.of(genre);
    }

    @Override
    public void insertGenresByFilmId(int filmId, List<Integer> genres) {
        String sql = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (:filmId, :genres)";

        List<FilmGenres> filmGenresList = new ArrayList<>(genres.size());
        for (Integer genreId : genres) {
            var filmGenre = new FilmGenres();
            filmGenre.setFilmId(filmId);
            filmGenre.setGenres(genreId);
            filmGenresList.add(filmGenre);
        }
        jdbcOperations.batchUpdate(sql, SqlParameterSourceUtils.createBatch(filmGenresList.toArray()));

    }

    @Override
    public List<Film> loadGenre(List<Film> films) {
        List<Integer> ids = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());

        String sql = "SELECT FILM_ID, G2.GENRE_ID, GENRE_NAME FROM FILM_GENRES " +
                "JOIN GENRES G2 on G2.GENRE_ID = FILM_GENRES.GENRE_ID " +
                "where FILM_ID in (:ids)";

        var sqlRowSet = jdbcOperations.queryForRowSet(sql, new MapSqlParameterSource("ids", ids));

        Map<Integer, Genre> genres = new HashMap<>();
        while (sqlRowSet.next()) {
            genres.put(sqlRowSet.getInt("FILM_ID"), Genre.builder()
                    .id(sqlRowSet.getInt("GENRE_ID"))
                    .name(sqlRowSet.getString("GENRE_NAME"))
                    .build());
        }
        Map<Integer, Film> mapFilm = films.stream()
                .collect(Collectors.toMap(Film::getId, Function.identity(), (a, b) -> b));

        for (Integer id : ids) {
            if (genres.containsKey(id)) {
                mapFilm.get(id).addGenre(genres.get(id));
            }
        }
        return new ArrayList<>(mapFilm.values());
    }
}
