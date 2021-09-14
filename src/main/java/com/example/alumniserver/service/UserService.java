package com.example.alumniserver.service;

import com.example.alumniserver.dao.UserRepository;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
        oldUser = updateUserInformation(oldUser, user);
        return repository.save(oldUser);
    }

    private User updateUserInformation(User oldUser, User user) {
        if (user.getBio() != null) {
            oldUser.setBio(user.getBio());
        }
        if (user.getFunFact() != null) {
            oldUser.setFunFact(user.getFunFact());
        }
        if (user.getPicture() != null) {
            oldUser.setPicture(user.getPicture());
        }
        if (user.getStatus() != null) {
            oldUser.setStatus(user.getStatus());
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }

        return oldUser;
    }

    public User addEventToUser(Event event, User user) {
        return (user.addEventToUser(event)) ? repository.save(user) : null;
    }

    public User deleteEventFromUser(Event event, User user) {
        return (user.deleteEventToUser(event)) ? repository.save(user) : null;
    }

}
