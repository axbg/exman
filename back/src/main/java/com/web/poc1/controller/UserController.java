package com.web.poc1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;

@Controller
@RequestMapping("/api/user")
public class UserController {

    @GetMapping
    @ResponseBody
    public String getUsers() {
        return "getting users";
    }

    @PostMapping
    @ResponseBody
    public String createUser() {
        return "creating user";
    }

    @DeleteMapping
    @ResponseBody
    public String deleteUser(@PathParam("id") int id) {
        return "deleting users";
    }

}
