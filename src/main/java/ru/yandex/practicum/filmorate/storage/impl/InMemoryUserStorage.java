package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();
    private static Integer globalId = 1;

    @Override
    public Optional<User> addUser(User user) {
        if (users.containsValue(user)) {
            return Optional.empty();
        }
        var userBuild = user.toBuilder()
                .friends(new HashSet<>())
                .id(generateId())
                .build();
        users.put(userBuild.getId(), userBuild);
        return Optional.of(users.get(userBuild.getId()));
    }

    @Override
    public Optional<User> updateUser(User user) {
        var userDb = users.get(user.getId());
        if (userDb != null) {
            BeanUtils.copyProperties(user, userDb, "id", "friends");
            return Optional.of(userDb);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAllUsers() {
        if (users.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean deleteUser(int id) {
        return Optional.ofNullable(users.remove(id)).isPresent();
    }

    private int generateId() {
        return globalId++;
    }
}
