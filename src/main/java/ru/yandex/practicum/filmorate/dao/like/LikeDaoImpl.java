package ru.yandex.practicum.filmorate.dao.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class LikeDaoImpl implements LikeDao {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO LIKES(FILM_ID, USER_ID) " +
                "VALUES (:filmId, :userId)";

        jdbcOperations.update(sql, new MapSqlParameterSource(Map.of("filmId", filmId, "userId", userId)));
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sql = "DELETE FROM LIKES " +
                "WHERE FILM_ID = :filmId AND USER_ID = :userId";

        jdbcOperations.update(sql, new MapSqlParameterSource(Map.of("filmId", filmId, "userId", userId)));
    }

    @Override
    public List<Film> findPopular(int count) {
        String sql = "SELECT * FROM FILMS F " +
                "join LIKES L on F.FILM_ID = L.FILM_ID " +
                "join MPA M on F.MPA_ID = M.MPA_ID " +
                "group by F.FILM_ID " +
                "order by count(L.FILM_ID) desc " +
                "limit :count";
        return jdbcOperations.query(sql, new MapSqlParameterSource(Map.of("count", count)), new FilmRowMapper());
    }

    @Override
    public Set<Integer> findLikeByFilmId(int filmId) {
        String sql = "SELECT USER_ID FROM LIKES " +
                "WHERE FILM_ID = :filmId";
        Set<Integer> usersIds = new HashSet<>();
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(sql, new MapSqlParameterSource(Map.of("filmId", filmId)));
        if (rowSet.next()) {
            usersIds.add(rowSet.getInt("USER_ID"));
        }
        return usersIds;
    }
}

