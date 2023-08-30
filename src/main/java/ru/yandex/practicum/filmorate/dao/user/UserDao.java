package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> addUser(User user);

    Optional<User> updateUser(User user);

    void deleteUser(int userId);

    Optional<User> findUserById(Integer id);

    List<User> findAllUsers();

    boolean userExist(int userId);

}
