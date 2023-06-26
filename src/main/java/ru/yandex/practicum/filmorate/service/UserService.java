package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    User updateUser(User user);

    User findUserById(int id);

    List<User> getAllUser();

    void deleteUser(int id);

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> findAllFriends(int id);

    List<User> findCommonFriends(int id, int otherId);
}
