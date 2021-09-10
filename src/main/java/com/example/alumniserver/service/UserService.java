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

    public User getUserById(String userId) {
        return repository.findUserById(userId);
    }

    public User addUser(User user) {
        return repository.save(user);
    }

    public boolean userExists(String userId) {
        return repository.existsById(userId);
    }

    public User updateUser(String userId, User user) {
        User oldUser = repository.findUserById(userId);
        return updateUserInformation(oldUser, user);
    }

    private User updateUserInformation(User oldUser, User user) {
        if (!user.getBio().equals("")) {
            oldUser.setBio(user.getBio());
        }
        if (!user.getFunFact().equals("")) {
            oldUser.setBio(user.getFunFact());
        }
        if (!user.getName().equals("")) {
            oldUser.setName(user.getName());
        }
        if (!user.getPicture().equals("")) {
            oldUser.setPicture(user.getPicture());
        }
        if(!user.getStatus().equals("")) {
            oldUser.setStatus(user.getStatus());
        }

        return oldUser;
    }

}
