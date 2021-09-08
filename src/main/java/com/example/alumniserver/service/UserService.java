package com.example.alumniserver.service;

import com.example.alumniserver.dao.UserRepository;
import com.example.alumniserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(String userId) {
        return repository.findUserById(userId);
    }

    public User addUser(User user) {
        return repository.save(user);
    }

    public User updateUser(String userId, User user) {
        User oldUser = repository.findUserById(userId);
        return updateUserInformation(oldUser, user);
    }

    private User updateUserInformation(User oldUser, User user) {
        if (user.getBio() != "") {
            oldUser.setBio(user.getBio());
        }
        if (user.getFunFact() != "") {
            oldUser.setBio(user.getFunFact());
        }
        if (user.getName() != "") {
            oldUser.setName(user.getName());
        }
        if (user.getPicture() != "") {
            oldUser.setPicture(user.getPicture());
        }
        if(user.getStatus() != "") {
            oldUser.setStatus(user.getStatus());
        }

        return oldUser;
    }

}
