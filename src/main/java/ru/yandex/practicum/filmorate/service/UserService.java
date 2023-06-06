package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
@Service
public class UserService {
    private HashMap<Integer, User> users;
    private int count;

    public UserService() {
        this.users = new HashMap<>();
        count = 1;
    }

    public User saveUser(User user) {
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (users.containsValue(user)) {
            for (User value : users.values()) {
                if (value.equals(user)) return value;
            }
        }
        user.setId(count);
        users.put(count, user);
        count++;
        return user;
    }

    public User changeUser(User user) {
        Optional<User> userOpt = Optional.ofNullable(users.get(user.getId()));
        if (userOpt.isEmpty()) {
            throw new NotFoundException("This user does not exist");
        }
        User userDb = userOpt.get();
        BeanUtils.copyProperties(user, userDb, "id");
        return users.put(user.getId(), userDb);
    }

    public List<User> getAllUser() {
        if (users.isEmpty()) {
            throw new NotFoundException("The list of users is empty");
        }
        return new ArrayList<>(users.values());
    }
}
