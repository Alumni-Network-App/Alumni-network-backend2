package com.example.alumniserver.controller;


import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.User;
import com.example.alumniserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<Link> UpdateUser(
            @PathVariable String userId,
            @RequestBody User user) {
        User updateUser = service.updateUser(userId, user);
        return new ResponseEntity<>(
                getUserLinkById(updateUser.getId()),
                httpStatusCode.getContentStatus());
    }

    @GetMapping
    public ResponseEntity<Link> getUserLink() {
        String id = "2";
        Link link = getUserLinkById(id);
        return new ResponseEntity<>(link, HttpStatus.SEE_OTHER);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        User user = service.getUserById(userId);
        return new ResponseEntity<>(
                user,
                httpStatusCode.getFoundStatus(user));
    }


    @PostMapping
    public ResponseEntity<Link> addUser(@RequestBody User user) {
        User addedUser = service.addUser(user);

        return new ResponseEntity<>(getUserLinkById(
                addedUser.getId()),
                HttpStatus.CREATED);
    }

    private Link getUserLinkById(String userId) {
        return linkTo(methodOn(UserController.class)
                .getUserById(userId))
                .withSelfRel();
    }

}