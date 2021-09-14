package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.model.User;
import com.example.alumniserver.service.GroupService;
import com.example.alumniserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final UserService userService;

    private static final String TEST_ID = "3";

    private HttpStatusCode status = new HttpStatusCode();

    @Autowired
    public GroupController(GroupService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Group>> getGroups(Pageable page, @RequestParam(required = false, defaultValue = "") String name) {
        String id = TEST_ID;
        return new ResponseEntity<>(service.getGroups(id, name, page), HttpStatus.OK);
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<Group> getGroup(@PathVariable long groupId) {
        String id = TEST_ID;
        Group group = service.getGroup(groupId);
        HttpStatus httpStatus = (status.getBadRequestStatus(group) == HttpStatus.BAD_REQUEST) ?
                HttpStatus.BAD_REQUEST : status.getForbiddenStatus(
                !group.isPrivate() || group.isUserMember(id));
        if (httpStatus == HttpStatus.FORBIDDEN)
            group = null;
        return new ResponseEntity<>(group, httpStatus);
    }

    @PostMapping
    public ResponseEntity<Link> createGroup(@RequestBody Group group) {
        String userId = TEST_ID;
        Group addedGroup = service.createGroup(group, userId);
        if (group != null)
            return new ResponseEntity<>(
                    getGroupLinkById(addedGroup.getId()),
                    HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = {"/{groupId}/join", "/{groupId}/join/{userId}"})
    public ResponseEntity<Link> createGroupMembership(
            @PathVariable long groupId,
            @PathVariable(required = false) String userId
    ) {
        Group group = service.getGroup(groupId);
        String loggedInUserId = TEST_ID;
        if (group == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        else if (userId == null)
            userId = TEST_ID;

        if (group.isUserMember(userId))
            return new ResponseEntity<>(
                    getGroupLinkById(group.getId()),
                    HttpStatus.SEE_OTHER);
        else if (userId != loggedInUserId) {
            User user = userService.getUserById(userId);
            if (user == null)
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            group = service.addUserToGroup(group, user, loggedInUserId);
        } else
            group = service.createGroupMembership(group, userId);

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
