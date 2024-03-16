package com.example.jsonview.controller;

import com.example.jsonview.model.User;
import com.example.jsonview.model.Views;
import com.example.jsonview.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @JsonView(Views.Details.class)
    @GetMapping("/user/{user_id}")
    public User getUserById(@PathVariable(value = "user_id") Long id) {
        return userService.getUserById(id);
    }

    @JsonView(Views.Summary.class)
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/user")
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/user/{user_id}")
    public User updateUser(@Valid @RequestBody User user, @PathVariable(value = "user_id") Long id) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/user/{user_id}")
    public void removeUser(@PathVariable(value = "user_id") Long id) {
        userService.removeUser(id);
    }
}
