package com.example.alumniserver.service;

import com.example.alumniserver.dao.GroupRepository;
import com.example.alumniserver.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    private final GroupRepository repository;

    @Autowired
    public GroupService(GroupRepository repository) {
        this.repository = repository;
    }

    public List<Group> getGroups(long userId) {
        List<Group> nonPrivateGroups = repository.findGroupsByIsPrivate(false);
        List<Group> privateGroups = getPrivateGroups(userId);
        privateGroups.addAll(nonPrivateGroups);
        return privateGroups;
    }

    public Group getGroup(long groupId) {
        return repository.findGroupsById(groupId);
    }

    private List<Group> getPrivateGroups(long userId) {
        return filterOutGroupsNotMember(repository.findGroupsByIsPrivate(true), userId);
    }

    private List<Group> filterOutGroupsNotMember(List<Group> groups, long userId) {
        List<Group> returnGroups = new ArrayList<>();
        for(Group group : groups) {
            if(group.isUserMember(userId))
                returnGroups.add(group);
        }
        return returnGroups;
    }
}
