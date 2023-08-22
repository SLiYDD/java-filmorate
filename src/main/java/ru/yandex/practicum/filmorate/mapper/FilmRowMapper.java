package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;


public class FilmRowMapper implements RowMapper<Film> {


    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("FILM_ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(Mpa.builder().id(rs.getInt("MPA_ID"))
                        .name(rs.getString("MPA.NAME"))
                        .build())
                .likes(new HashSet<>())
                .genres(new HashSet<>())
                .build();
    }
}