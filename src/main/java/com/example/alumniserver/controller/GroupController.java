package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.idhelper.IdHelper;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.model.User;
import com.example.alumniserver.service.GroupService;
import com.example.alumniserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private HttpStatusCode status = new HttpStatusCode();

    @Autowired
    public GroupController(GroupService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Group>> getGroups(Pageable page, @RequestParam(required = false, defaultValue = "") String name) {
        String userId = IdHelper.getLoggedInUserId();
        return new ResponseEntity<>(service.getGroups(userId, name, page), HttpStatus.OK);
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<Group> getGroup(@PathVariable long groupId) {
        String userId = IdHelper.getLoggedInUserId();
        Group group = service.getGroup(groupId);
        HttpStatus httpStatus = (status.getBadRequestStatus(group) == HttpStatus.BAD_REQUEST) ?
                HttpStatus.BAD_REQUEST : status.getForbiddenStatus(
                !group.isPrivate() || group.isUserMember(userId));
        if (httpStatus == HttpStatus.FORBIDDEN)
            group = null;
        return new ResponseEntity<>(group, httpStatus);
    }

    @PostMapping
    public ResponseEntity<Link> createGroup(@RequestBody Group group) {
        String userId = IdHelper.getLoggedInUserId();
        Group addedGroup = service.createGroup(group, userId);
        return new ResponseEntity<>(
                getGroupLinkById(addedGroup),
                status.getBadRequestPostingStatus(addedGroup));
    }

    @PostMapping(value = {"/{groupId}/join", "/{groupId}/join/{userId}"})
    public ResponseEntity<Link> createGroupMembership(
            @PathVariable long groupId,
            @PathVariable(required = false) String userId
    ) {
        Group group = service.getGroup(groupId);
        String loggedInUserId = IdHelper.getLoggedInUserId();
        if (group == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        else if (userId == null)
            userId = IdHelper.getLoggedInUserId();

        if (group.isUserMember(userId))
            return new ResponseEntity<>(
                    getGroupLinkById(group),
                    HttpStatus.SEE_OTHER);
        else if (!userId.equals(loggedInUserId)) {
            User user = userService.getUserById(userId);
            if (user == null)
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            group = service.addUserToGroup(group, user, loggedInUserId);
        } else
            group = service.createGroupMembership(group, userId);

        return new ResponseEntity<>(getGroupLinkById(group),
                status.getForbiddenPostingStatus(group));
    }

    private Link getGroupLinkById(Group group) {
        return (group == null) ? null :
                linkTo(methodOn(GroupController.class)
                        .getGroup(group.getId()))
                        .withSelfRel();
    }

}
