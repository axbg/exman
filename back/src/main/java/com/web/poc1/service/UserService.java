package com.web.poc1.service;

import com.web.poc1.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User login(final String username, final String password);

    User register(final User user, final String username, final String password, final boolean isAdmin);

    Optional<User> findByToken(String token);

    List<User> getUsers(User user);

    void deleteUser(User user, Long id);

    void logout(User user);
}
