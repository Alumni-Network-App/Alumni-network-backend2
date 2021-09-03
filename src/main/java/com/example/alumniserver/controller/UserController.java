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


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = service.getAllUsers();
        return new ResponseEntity<>(users, httpStatusCode.getFoundStatus(users));
    }


    //TODO
    //Fixa LÄNKEN
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public ResponseEntity<String> getUserByQueryId(@RequestHeader int user_id){
        return new ResponseEntity<>("/user", httpStatusCode.getSeeOtherCode());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<User> addNewUser(@RequestBody User user){
        return new ResponseEntity<>(service.addUser(user), httpStatusCode.getContentStatus());
    }

    @Modifying
    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public ResponseEntity<User> UpdateUser(@RequestBody int id, User user){
        return new ResponseEntity<>(service.updateUser(user), httpStatusCode.getContentStatus());
    }


}