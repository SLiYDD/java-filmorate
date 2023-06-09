package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    Optional<User> addUser(User user);

    Optional<User> updateUser(User user);

    List<User> findAllUsers();

    Optional<User> findUserById(Integer id);

    boolean deleteUser(int id);
}
