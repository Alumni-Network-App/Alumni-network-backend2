package com.example.alumniserver.controller;


import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.User;
import com.example.alumniserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService service;
    private HttpStatusCode httpStatusCode = new HttpStatusCode();

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PatchMapping(value = "/update/{userId}")
    public ResponseEntity<User> UpdateUser(
            @PathVariable String userId,
            @RequestBody User user){
        return new ResponseEntity<>(service.updateUser(userId, user), httpStatusCode.getContentStatus());
    }

    // Detta är fel, behöver retunera en länk till den inloggade användaren + ett 303 status
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = service.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        User user = service.getUserById(userId);
        return new ResponseEntity<>(user, httpStatusCode.getFoundStatus(user));
    }


    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User addedUser =  service.addUser(user);
        // Return a location -> url to get the new resource
        return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
    }


}