package com.example.alumniserver.service;

import com.example.alumniserver.dao.GroupRepository;
import com.example.alumniserver.dao.UserRepository;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<Group> getGroups(String userId, String name, Pageable page) {
        List<Group> groups = repository.findGroups(userId, name, page).getContent();
        groups.forEach(group -> System.out.print(group.getGroupId() + ", "));
        System.out.println();
        return groups;
    }

    public Group getGroup(long groupId) {
        return repository.findGroupByGroupId(groupId);
    }

    public Group createGroup(Group group, String userId) {
        User user = userRepository.findUserById(userId);
        group.setLastUpdated();
        return createMembership(group, user);
    }

    public Group createGroupMembership(Group group, String userId) {
        return (group.isPrivate())
                ? null : createMembership(group, userRepository.findUserById(userId));
    }

    public Group addUserToGroup(Group group, User user, String loggedInUserId) {
        return (group.isUserMember(loggedInUserId))
                ? createMembership(group, user) : null;
    }

    public Group removeEventFromGroup(Event event, Group group) {
        return (group.removeEventFromGroup(event))
                ? repository.save(group) : null;
    }

    private Group createMembership(Group group, User user) {
        if(group.getName() != null) {
            group.addUserAsMember(user);
            return repository.save(group);
        } else
            return null;
    }

    public boolean groupExists(long groupId) {
        return repository.existsById(groupId);
    }

    public void updateGroupTime(long groupId, LocalDateTime lastUpdated) {
        repository.updateGroup(groupId, lastUpdated);
    }
}
