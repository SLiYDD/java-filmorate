package ru.yandex.practicum.filmorate.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.impl.BaseUserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final BaseUserService baseUserService;

    @Autowired
    public UserController(BaseUserService baseUserService) {
        this.baseUserService = baseUserService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление пользователя");
        return baseUserService.saveUser(user);
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody @NotNull User user) {
        log.info("Получен запрос на обновление пользователя");
        return baseUserService.updateUser(user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User findUserById(@PathVariable("id") int userId) {
        log.info("Получен запрос на получение пользователя по ID");
        return baseUserService.findUserById(userId);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return baseUserService.getAllUser();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") @NotNull int id) {
        log.info("Получен запрос на удаления пользователя");
        baseUserService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.info("Получен запрос на добавления в друзья");
        baseUserService.addFriend(id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.info("Получен запрос на удаления из друзей");
        baseUserService.deleteFriend(id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAllFriends(@PathVariable("id") int id) {
        log.info("Получен запрос на получение всех друзей");
        return baseUserService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findCommonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        log.info("Получен запрос на получение общих друзей");
        return baseUserService.findCommonFriends(id, otherId);
    }

}

