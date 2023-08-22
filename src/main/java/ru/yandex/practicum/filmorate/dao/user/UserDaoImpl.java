package ru.yandex.practicum.filmorate.dao.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Optional<User> addUser(User user) {
        String sql = "INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY)" +
                "VALUES(:email, :login, :name, :birthday)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("email", user.getEmail());
        map.addValue("login", user.getLogin());
        map.addValue("name", user.getName());
        map.addValue("birthday", user.getBirthday());
        jdbcOperations.update(sql, map, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        String sql = "UPDATE USERS SET EMAIL = :email" +
                ", LOGIN = :login, NAME = :name, BIRTHDAY = :birthday " +
                "where USER_ID = :userId";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("email", user.getEmail());
        map.addValue("login", user.getLogin());
        map.addValue("name", user.getName());
        map.addValue("birthday", user.getBirthday());
        map.addValue("userId", user.getId());
        jdbcOperations.update(sql, map);
        return Optional.of(user);
    }

    @Override
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM USERS";
        return jdbcOperations.query(sql, new UserRowMapper());
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        String sql = "SELECT * FROM USERS " +
                "WHERE USER_ID  = :userId";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", id);
        final List<User> users = jdbcOperations.query(sql, Map.of("userId", id), new UserRowMapper());
        if (users.size() == 1) {
            var user = users.get(0);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public boolean userExist(int userId) {
        String sql = "SELECT USER_ID FROM USERS " +
                "WHERE USER_ID = :userId";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        List<Integer> user = jdbcOperations.query(sql, map, ((rs, rowNum) -> rs.getInt("USER_ID")));
        return !user.isEmpty();
    }

    @Override
    public void deleteUser(int userId) {
        String sql = "DELETE FROM USERS " +
                "WHERE USER_ID = :userId";
        var map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        jdbcOperations.update(sql, map);
    }
}

