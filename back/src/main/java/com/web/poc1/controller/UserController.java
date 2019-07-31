package com.web.poc1.controller;

import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping
    public String getUsers() {
        return "getting users";
    }

    @PostMapping
    public String createUser() {
        return "creating user";
    }

    @DeleteMapping
    public String deleteUser(@PathParam("id") int id) {
        return "deleting users";
    }

}
