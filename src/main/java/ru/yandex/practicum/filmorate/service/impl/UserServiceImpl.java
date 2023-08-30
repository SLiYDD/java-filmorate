package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.friend.FriendDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ResourceAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final FriendDao friendDao;

    @Override
    public User saveUser(User user) {
        validName(user);
        Optional<User> userOpt = userDao.addUser(user);
        if (userOpt.isPresent()) {
            var saveUser = userOpt.get();
            saveUser.setFriends(new HashSet<>());
            return saveUser;
        }
        throw new ResourceAlreadyExistException("Ошибка добавления! Возможно такой пользователь уже существует");
    }

    private void validName(User user) {
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }


    @Override
    public User updateUser(User user) {
        if (userDao.userExist(user.getId())) {
            Optional<User> userOpt = userDao.updateUser(user);
            User updateUser = userOpt.get();
            updateUser.setFriends(getFriendsIds(user.getId()));
            return updateUser;
        }
        throw new NotFoundException("Пользователь с id : " + user.getId() + " не найден");
    }


    @Override
    public User findUserById(int userId) {
        Optional<User> userOpt = userDao.findUserById(userId);
        if (userOpt.isEmpty()) {
            throw new NotFoundException("Пользователь с id : " + userId + " не найден");
        }
        User getUser = userOpt.get();

        getUser.setFriends(getFriendsIds(userId));
        return getUser;
    }


    @Override
    public List<User> getAllUser() {
        List<User> listUser = userDao.findAllUsers();
        if (listUser.isEmpty()) {
            throw new NotFoundException("Список людей пуст");
        }
        listUser.forEach(user -> user.setFriends(getFriendsIds(user.getId())));
        return listUser;
    }

    private Set<Integer> getFriendsIds(int userId) {
        return friendDao.findUserFiends(userId).stream()
                .map(User::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteUser(int userId) {
        if (userDao.userExist(userId)) {
            throw new NotFoundException("Пользователь с id : " + userId + " не найден");
        }
        userDao.deleteUser(userId);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (!userDao.userExist(userId)) {
            throw new NotFoundException("Пользователь с id : " + userId + " не найден");
        }
        if (!userDao.userExist(friendId)) {
            throw new NotFoundException("Пользователь с id : " + userId + " не найден");
        }
        friendDao.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        if (!userDao.userExist(userId)) {
            throw new NotFoundException("Пользователь с id : " + userId + " не найден");
        }
        if (!userDao.userExist(friendId)) {
            throw new NotFoundException("Пользователь с id : " + userId + " не найден");
        }
        friendDao.deleteFriend(userId, friendId);

    }

    @Override
    public List<User> findAllFriends(int userId) {
        if (userDao.userExist(userId)) {
            return new ArrayList<>(friendDao.findUserFiends(userId));
        }
        throw new NotFoundException("Пользователь с id : " + userId + " не найден");
    }

    @Override
    public List<User> findCommonFriends(int id, int otherId) {
        if (!userDao.userExist(id)) {
            throw new NotFoundException("Пользователь с id : " + id + " не найден");
        }
        if (!userDao.userExist(otherId)) {
            throw new NotFoundException("Пользователь с id : " + otherId + " не найден");
        }
        return new ArrayList<>(friendDao.findCommonFriends(id, otherId));
    }
}

