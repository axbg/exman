package com.web.poc1.controller;

import com.web.poc1.exception.CustomException;
import com.web.poc1.model.User;
import com.web.poc1.service.UserService;
import com.web.poc1.util.MessageHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<User> login(@RequestParam("username") final String username, @RequestParam("password") final String password)
            throws CustomException {
        User user = userService.login(username, password);

        if (user == null) {
            throw new CustomException("Bad credentials", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<User> register(@AuthenticationPrincipal final User currentUser, @RequestParam("username") final String username,
                                         @RequestParam("password") final String password,
                                         @RequestParam("isAdmin") final boolean isAdmin)
            throws CustomException {
        User user = userService.register(currentUser, username, password, isAdmin);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@AuthenticationPrincipal final User user) {
        return new ResponseEntity<>(userService.getUsers(user), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<MessageHolder> deleteUser(@AuthenticationPrincipal final User user, @PathVariable Long id) {
        userService.deleteUser(user, id);
        return new ResponseEntity<>(new MessageHolder("User deleted"), HttpStatus.OK);
    }

}
