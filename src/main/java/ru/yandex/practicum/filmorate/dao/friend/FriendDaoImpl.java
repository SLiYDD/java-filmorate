package ru.yandex.practicum.filmorate.dao.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class FriendDaoImpl implements FriendDao {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void addFriend(int userId, int friendId) {
        String sql = "INSERT INTO FRIENDS(USER_ID, FRIEND_ID, STATUS) " +
                "VALUES (:userId, :friendId, :status)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("friendId", friendId);
        map.addValue("status", true);
        jdbcOperations.update(sql, map);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sql = "DELETE FROM FRIENDS " +
                "WHERE USER_ID = :userId AND FRIEND_ID = :friendId";
        var map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("friendId", friendId);
        jdbcOperations.update(sql, map);
    }

    @Override
    public List<User> findUserFiends(int userId) {
        final String sql = "SELECT * FROM USERS " +
                "WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID = :userId)";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        return jdbcOperations.query(sql, map, new UserRowMapper());
    }

    @Override
    public Collection<User> findCommonFriends(int id, int otherId) {
        String sql = "SELECT * FROM (SELECT * " +
                "                    FROM USERS " +
                "                    WHERE USER_ID IN (SELECT FRIEND_ID " +
                "                                      FROM FRIENDS " +
                "                                      WHERE USER_ID = :id))" +
                "     WHERE USER_ID IN (SELECT USER_ID " +
                "                       FROM USERS " +
                "                       WHERE USERS.USER_ID IN (SELECT FRIEND_ID " +
                "                                               FROM FRIENDS " +
                "                                               WHERE USER_ID = :otherId))";
        return jdbcOperations.query(sql, new MapSqlParameterSource(Map.of("id", id, "otherId", otherId)), new UserRowMapper());
    }
}
