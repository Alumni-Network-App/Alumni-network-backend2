package com.example.alumniserver.controller;


import com.example.alumniserver.data_access.UserRepository;
import com.example.alumniserver.models.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class UserController {

    private UserRepository userRepository = new UserRepository();


    @RequestMapping(value="/api/users", method = RequestMethod.GET)
    public ArrayList<User> getAllUsers(){
        return userRepository.getAllUsers();
    }

    @RequestMapping(value = "api/user", method = RequestMethod.GET)
    public User getUserByQueryId(@RequestHeader int user_id){
        return userRepository.getUserById(user_id);
    }

    @RequestMapping(value = "api/users/add", method = RequestMethod.POST)
    public Boolean addNewUser(@RequestBody User user){
        return userRepository.addUser(user);
    }

    @RequestMapping(value = "api/user/update", method = RequestMethod.PATCH)
    public Boolean UpdateUser(@RequestBody int id, User user){
        return userRepository.updateUser(user);
    }


}