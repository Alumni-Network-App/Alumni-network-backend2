package com.example.alumniserver.controller;


import com.example.alumniserver.data_access.UserRepository;
import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserRepository userRepository;
    private HttpStatusCode httpStatusCode = new HttpStatusCode();

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, httpStatusCode.getFoundStatus(users));
    }


    //TODO
    //Fixa LÄNKEN
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public ResponseEntity<String> getUserByQueryId(@RequestHeader int user_id){
        return new ResponseEntity<>("/user", HttpStatus.SEE_OTHER);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<User> addNewUser(@RequestBody User user){
        return new ResponseEntity<>(userRepository.save(user), httpStatusCode.getContentStatus());
    }

    @Modifying
    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public ResponseEntity<User> UpdateUser(@RequestBody int id, User user){
        return new ResponseEntity<>(userRepository.save(user), httpStatusCode.getContentStatus());
    }


}