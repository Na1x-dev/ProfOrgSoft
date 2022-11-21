package com.example.demo.services.user;

import com.example.demo.models.Role;
import com.example.demo.models.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface UserService {
    void create(User user);

    User findByUsername(String username);

    List<User> readAll();

    boolean update(User user, Long id);

    boolean delete(Long id);
}
