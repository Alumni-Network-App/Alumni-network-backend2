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
    private IdHelper idHelper = new IdHelper();

    @Autowired
    public GroupController(GroupService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Group>> getGroups(Pageable page,
                                                 @RequestParam(required = false, defaultValue = "") String name,
                                                 @RequestHeader("Authorization") String auth) {
        String userId = idHelper.getLoggedInUserId(auth);
        List<Group> groups = service.getGroups(userId, name, page);
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<Group> getGroup(@PathVariable long groupId, @RequestHeader("Authorization") String auth) {
        String userId = idHelper.getLoggedInUserId(auth);
        Group group = service.getGroup(groupId);
        HttpStatus httpStatus = (status.getBadRequestStatus(group) == HttpStatus.BAD_REQUEST) ?
                HttpStatus.BAD_REQUEST : status.getForbiddenStatus(
                !group.isPrivate() || group.isUserMember(userId));
        if (httpStatus == HttpStatus.FORBIDDEN)
            group = null;
        return new ResponseEntity<>(group, httpStatus);
    }

    @PostMapping
    public ResponseEntity<Link> createGroup(@RequestBody Group group, @RequestHeader("Authorization") String auth) {
        String userId = idHelper.getLoggedInUserId(auth);
        Group addedGroup = service.createGroup(group, userId);
        return new ResponseEntity<>(
                getGroupLinkById(addedGroup, auth),
                status.getBadRequestPostingStatus(addedGroup));
    }

    @PostMapping(value = {"/{groupId}/join", "/{groupId}/join/{userId}"})
    public ResponseEntity<Link> createGroupMembership(
            @PathVariable long groupId,
            @PathVariable(required = false) String userId,
            @RequestHeader("Authorization") String auth
    ) {
        Group group = service.getGroup(groupId);
        String loggedInUserId = idHelper.getLoggedInUserId(auth);
        if (group == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        else if (userId == null)
            userId = idHelper.getLoggedInUserId(auth);


        if (group.isUserMember(userId))
            return new ResponseEntity<>(
                    getGroupLinkById(group, auth),
                    HttpStatus.SEE_OTHER);
        else if (!userId.equals(loggedInUserId)) {
            User user = userService.getUserById(userId);
            if (user == null)
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            group = service.addUserToGroup(group, user, loggedInUserId);
        } else {
            group = service.createGroupMembership(group, userId);
        }

        return new ResponseEntity<>(getGroupLinkById(group, auth),
                status.getForbiddenPostingStatus(group));
    }

    private Link getGroupLinkById(Group group, String auth) {
        return (group == null) ? null :
                linkTo(methodOn(GroupController.class)
                        .getGroup(group.getGroupId(), auth))
                        .withSelfRel();
    }

}
