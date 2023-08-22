package ru.yandex.practicum.filmorate.dao.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendDao {
    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    Collection<User> findUserFiends(int userId);

    Collection<User> findCommonFriends(int id, int otherId);

}
