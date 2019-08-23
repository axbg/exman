package com.web.poc1.service;

import com.web.poc1.dao.UserRepository;
import com.web.poc1.exception.CustomException;
import com.web.poc1.model.User;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public User login(final String username, final String password) throws CustomException {
        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent() || !user.get().getPassword().equals(encryptPassword(password))) {
            throw new CustomException("Bad credentials", HttpStatus.BAD_REQUEST);
        }

        return user.get();
    }

    @Override
    public User register(final User currentUser, final String username, final String password, final boolean isAdmin) throws CustomException {
        checkIfAdmin(currentUser);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            throw new CustomException("Username already in use", HttpStatus.BAD_REQUEST);
        }

        User user = new User();

        user.setUsername(username);
        user.setToken(RandomStringUtils.randomAlphanumeric(250));
        user.setPassword(encryptPassword(password));
        user.setAdmin(isAdmin);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByToken(final String token) {
        return userRepository.findByToken(token);
    }

    @Override
    public List<User> getUsers(User user) {
        checkIfAdmin(user);
        return ((List<User>) userRepository.findAll()).stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(User user, Long id) {
        checkIfAdmin(user);
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            //user not found - nothing to do
        }
    }

    @Override
    public void logout(final User user) {

    }

    private String encryptPassword(String password) {
        return DigestUtils.sha1Hex(password);
    }

    private void checkIfAdmin(User user) {
        if (!user.isAdmin()) {
            throw new CustomException("Admin permission needed", HttpStatus.FORBIDDEN);
        }
    }
}
