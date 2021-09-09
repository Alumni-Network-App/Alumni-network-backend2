package com.example.alumniserver.service;

import com.example.alumniserver.dao.GroupRepository;
import com.example.alumniserver.dao.UserRepository;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    private final GroupRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<Group> getGroups(String userId) {
        List<Group> nonPrivateGroups = repository.findGroupsByIsPrivate(false);
        List<Group> privateGroups = getPrivateGroups(userId);
        privateGroups.addAll(nonPrivateGroups);
        return privateGroups;
    }

    public Group getGroup(long groupId) {
        return repository.findGroupById(groupId);
    }

    private List<Group> getPrivateGroups(String userId) {
        return filterOutGroupsNotMember(repository.findGroupsByIsPrivate(true), userId);
    }

    private List<Group> filterOutGroupsNotMember(List<Group> groups, String userId) {
        List<Group> returnGroups = new ArrayList<>();
        for(Group group : groups) {
            if(group.isUserMember(userId))
                returnGroups.add(group);
        }
        return returnGroups;
    }

    public Group createGroup(Group group, String userId) {
        User user = userRepository.findUserById(userId);
        return createMembership(group, user);
    }

    public Group createGroupMembership(long groupId, String userId) {
        Group group = repository.findGroupById(groupId);
        if(group.isPrivate() && !group.isUserMember(userId)) {
            return null;
        } else if(group.isUserMember(userId)) {
            //Detta är anledning till att ha en ResponseEntity skapas här.
            // Då status kod 403 är fel, men det kommer sluta med det om vi gör på detta sätt.
            return null;
        } else {
            User user = userRepository.findUserById(userId);
            return createMembership(group, user);
        }
    }

    public Group addUserToGroup(long groupId, String userId, String loggedInUserId) {
        Group group = repository.findGroupById(groupId);
        System.out.println("TEST1");
        if(group.isUserMember(userId))
            //Alla services kommer behöva ändras för att ta hänsyn till ResponseEntity
            return null;
        if(group.isUserMember(loggedInUserId)) {
            System.out.println("TEST2");
            User user = userRepository.findUserById(userId);
            System.out.println("TEST3");
            return createMembership(group, user);
        } else {
            return null;
        }
    }

    private Group createMembership(Group group, User user) {
        group.addUserAsMember(user);
        user.addGroup(group);
        userRepository.save(user);
        return repository.save(group);
    }

}
