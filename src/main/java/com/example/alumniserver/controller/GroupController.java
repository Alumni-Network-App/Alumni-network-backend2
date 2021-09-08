package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/group")
public class GroupController {

    private final GroupService service;

    private static final long TEST_ID = 2;

    private HttpStatusCode status = new HttpStatusCode();

    @Autowired
    public GroupController(GroupService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Group>> getGroups() {
        long id = TEST_ID;
        List<Group> groups = service.getGroups(id);
        return new ResponseEntity<>(groups, status.getFoundStatus(groups));
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<Group> getGroup(@PathVariable long groupId) {
        long id = TEST_ID;
        Group group = service.getGroup(groupId);
        HttpStatus httpStatus = (status.getFoundStatus(group) == HttpStatus.NOT_FOUND) ?
                HttpStatus.NOT_FOUND : status.getForbiddenStatus((group.isPrivate() && group.isUserMember(id)) || !group.isPrivate());
        if (httpStatus == HttpStatus.FORBIDDEN)
            group = null;
        return new ResponseEntity<>(group, httpStatus);
    }

    @PostMapping
    public ResponseEntity<Boolean> createGroup(@RequestBody Group group) {
        long userId = TEST_ID;
        boolean added = service.createGroup(group, userId);
        return new ResponseEntity<>(added, status.getContentStatus());
    }

    @PostMapping(value = {"/{groupId}/join", "/{groupId}/join/{userId}"})
    public ResponseEntity<Boolean> createGroupMembership(
            @PathVariable long groupId,
            @PathVariable(required = false) Long userId
    ) {
        boolean added;
        if (userId == null) {
            userId = TEST_ID;
            added = service.createGroupMembership(groupId, userId);
        } else {
            long loggedInUserId = TEST_ID;
            added = service.addUserToGroup(groupId, userId, loggedInUserId);
        }
        return new ResponseEntity<>(added, status.getForbiddenStatus(added));
    }

}
