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
        return repository.findGroupsById(groupId);
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

    public boolean createGroup(Group group, String userId) {
        User user = userRepository.findUserById(userId);
        return createMembership(group, user);
    }

    public boolean createGroupMembership(long groupId, String userId) {
        Group group = repository.findGroupsById(groupId);
        if(group.isPrivate() && !group.isUserMember(userId)) {
            return false;
        } else if(group.isUserMember(userId)) {
            return true;
        } else {
            User user = userRepository.findUserById(userId);
            return createMembership(group, user);
        }
    }

    public boolean addUserToGroup(long groupId, String userId, String loggedInUserId) {
        Group group = repository.findGroupsById(groupId);
        if(group.isUserMember(userId))
            return true;
        if(group.isUserMember(loggedInUserId)) {
            User user = userRepository.findUserById(userId);
            return createMembership(group, user);
        } else {
            return false;
        }
    }

    private boolean createMembership(Group group, User user) {
        group.addUserAsMember(user);
        repository.save(group);
        user.addGroup(group);
        userRepository.save(user);
        return true;
    }

}
