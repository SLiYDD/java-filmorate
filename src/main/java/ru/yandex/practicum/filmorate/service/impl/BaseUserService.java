package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ResourceAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaseUserService implements UserService {
    private final UserStorage storage;


    @Override
    public User saveUser(User user) {
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return storage.addUser(user)
                .orElseThrow(() -> new ResourceAlreadyExistException("Такой пользователь уже существует"));
    }


    @Override
    public User updateUser(User user) {
        return storage.updateUser(user)
                .orElseThrow(() -> new NotFoundException("Пользователь с id : " + user.getId() + " не найден"));
    }

    @Override
    public User findUserById(int id) {
        return storage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id : " + id + " не найден"));
    }


    @Override
    public List<User> getAllUser() {
        List<User> listUser = storage.findAllUsers();
        if (listUser.isEmpty()) {
            throw new NotFoundException("Список людей пуст");
        }
        return listUser;
    }

    @Override
    public void deleteUser(int id) {
        if (storage.deleteUser(id)) {
            return;
        }
        throw new NotFoundException("Пользователь с id : " + id + " не найден");
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = storage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id : " + userId + " не найден"));

        User friend = storage.findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Друг с id : " + friendId + " не найден"));
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = storage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id : " + userId + " не найден"));

        User friend = storage.findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Друг с id : " + friendId + " не найден"));
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    @Override
    public List<User> findAllFriends(int id) {
        User user = storage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id : " + id + " не найден"));

        return user.getFriends().stream()
                .map(integer -> storage.findUserById(integer).orElseThrow(() -> new NotFoundException("Общих друзей нет")))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriends(int id, int otherId) {
        User user = storage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id : " + id + " не найден"));

        User friend = storage.findUserById(otherId)
                .orElseThrow(() -> new NotFoundException("Друг с id : " + otherId + " не найден"));

        List<Integer> allFriends = concat(user.getFriends(), friend.getFriends());

        return allFriends.stream()
                .collect(Collectors.groupingBy(Function.identity()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() > 1)
                .map(entry -> storage.findUserById(entry.getKey()).get())
                .collect(Collectors.toList());
    }

    private List<Integer> concat(Set<Integer> friends, Set<Integer> otherFriend) {
        var result = new ArrayList<Integer>();
        result.addAll(friends);
        result.addAll(otherFriend);
        return result;
    }
}
