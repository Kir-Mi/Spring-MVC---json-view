package com.example.jsonview.service;

import com.example.jsonview.model.User;

import java.util.List;

public interface UserService {
    User getUserById(Long id);
    List<User> getAllUsers();
    User createUser(User user);
    void removeUser(Long id);
}
