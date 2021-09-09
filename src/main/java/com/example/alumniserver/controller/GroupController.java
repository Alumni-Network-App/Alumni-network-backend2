package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping("/api/v1/group")
public class GroupController {

    private final GroupService service;

    private static final String TEST_ID = "2";

    private HttpStatusCode status = new HttpStatusCode();

    @Autowired
    public GroupController(GroupService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Group>> getGroups() {
        String id = TEST_ID;
        List<Group> groups = service.getGroups(id);
        return new ResponseEntity<>(groups, status.getFoundStatus(groups));
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<Group> getGroup(@PathVariable long groupId) {
        String id = TEST_ID;
        Group group = service.getGroup(groupId);
        HttpStatus httpStatus = (status.getFoundStatus(group) == HttpStatus.NOT_FOUND) ?
                HttpStatus.NOT_FOUND : status.getForbiddenStatus(!group.isPrivate() || group.isUserMember(id));
        if (httpStatus == HttpStatus.FORBIDDEN)
            group = null;
        return new ResponseEntity<>(group, httpStatus);
    }

    @PostMapping
    public ResponseEntity<Link> createGroup(@RequestBody Group group) {
        String userId = TEST_ID;
        Group addedGroup = service.createGroup(group, userId);
        return new ResponseEntity<>(getGroupLinkById(addedGroup.getId()),
                status.getForbiddenPostingStatus(addedGroup));
    }

    @PostMapping(value = {"/{groupId}/join", "/{groupId}/join/{userId}"})
    public ResponseEntity<Link> createGroupMembership(
            @PathVariable long groupId,
            @PathVariable(required = false) String userId
    ) {
        Group group;
        if (userId == null) {
            userId = TEST_ID;
            group = service.createGroupMembership(groupId, userId);
        } else {
            String loggedInUserId = TEST_ID;
            group = service.addUserToGroup(groupId, userId, loggedInUserId);
        }
        Link link = (group != null) ? getGroupLinkById(group.getId()) : null;
        return new ResponseEntity<>(link,
                status.getForbiddenPostingStatus(group));
    }

    private Link getGroupLinkById(long groupId) {
        return linkTo(methodOn(GroupController.class)
                .getGroup(groupId))
                .withSelfRel();
    }

}
