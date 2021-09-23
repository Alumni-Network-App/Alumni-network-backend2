package com.example.alumniserver.controller;


import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.idhelper.IdHelper;
import com.example.alumniserver.model.Post;
import com.example.alumniserver.model.Reply;
import com.example.alumniserver.model.User;
import com.example.alumniserver.service.*;
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
@RequestMapping("/api/v1/post")
public class PostController {

    private IdHelper idHelper = new IdHelper();
    private final HttpStatusCode statusCode = new HttpStatusCode();
    private final PostService postService;
    private final UserService userService;
    private final GroupService groupService;
    private final TopicService topicService;
    private final EventService eventService;

    @Autowired
    public PostController(
            PostService postService,
            UserService userService,
            GroupService groupService,
            TopicService topicService,
            EventService eventService
    ) {
        this.postService = postService;
        this.userService = userService;
        this.groupService = groupService;
        this.topicService = topicService;
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPostsFromLoggedInUser(
            @RequestParam(required = false, defaultValue = "") String type,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestHeader("Authorization") String auth,
            Pageable page
    ) {
        String id = idHelper.getLoggedInUserId(auth);
        List<Post> posts = postService.getPosts(id, type, search, page);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<Post> getPost(
            @PathVariable long postId,
            @RequestHeader("Authorization") String auth
    ) {
        String id = idHelper.getLoggedInUserId(auth);

        Post post = postService.getPost(postId);
        if (post != null) {
            boolean isUsersPost = postService.isUsersPost(id, post);
            return new ResponseEntity<>(
                    isUsersPost ? post : null,
                    statusCode.getForbiddenStatus(isUsersPost));
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/user")
    public ResponseEntity<List<Post>> getPostsToLoggedInUser(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestHeader("Authorization") String auth,
            Pageable page
    ) {
        String id = idHelper.getLoggedInUserId(auth);
        return getPostsToType(true, "user", id, id, search, page);
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<Post>> getPostsToUserFromSpecificUser(
            @PathVariable String userId,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestHeader("Authorization") String auth,
            Pageable page) {
        String id = idHelper.getLoggedInUserId(auth);
        if(userService.userExists(userId)) {
            List<Post> posts = postService.getPostsSentToUser("user", id, userId, search, page);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/group/{groupId}")
    public ResponseEntity<List<Post>> getPostsToGroup(
            @PathVariable String groupId,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestHeader("Authorization") String auth,
            Pageable page) {
        String id = idHelper.getLoggedInUserId(auth);
        boolean groupFound = groupService.groupExists(Long.parseLong(groupId));
        return getPostsToType(groupFound, "group", groupId, id, search, page);
    }

    @GetMapping(value = "/topic/{topicId}")
    public ResponseEntity<List<Post>> getPostsWithTopic(
            @PathVariable long topicId,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestHeader("Authorization") String auth,
            Pageable page) {
        boolean topicExists = topicService.topicExists(topicId);
        String userId = idHelper.getLoggedInUserId(auth);
        return (!topicExists) ?
                new ResponseEntity<>(null, HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(postService
                        .getPostsFromUserToTopic(topicId, userId, search, page),
                        HttpStatus.OK);
    }

    @GetMapping(value = "/event/{eventId}")
    public ResponseEntity<List<Post>> getPostsToEvent(
            @PathVariable String eventId,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestHeader("Authorization") String auth,
            Pageable page) {
        String id = idHelper.getLoggedInUserId(auth);
        boolean eventExists = eventService.eventExists(Long.parseLong(eventId));
        return getPostsToType(eventExists, "event", eventId, id, search, page);
    }

    @PostMapping
    public ResponseEntity<Link> createPost(
            @RequestBody Post post,
            @RequestHeader("Authorization") String auth
    ) {
        String id = idHelper.getLoggedInUserId(auth);
        if (post.getReceiverType() == null
                || post.getReceiverId() == null
                || post.getTopic() == null
                || !topicService.topicExists(post.getTopic().getId())
                || !receiverExists(post.getReceiverType(), post.getReceiverId())
        ) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        post = postService.createPost(post, id);
        if(post != null && post.getReceiverType().equals("group"))
            groupService.updateGroupTime(Long.parseLong(post.getReceiverId()), post.getLastUpdated());

        return new ResponseEntity<>((post != null) ? getPostLinkById(post.getId(), auth) : null,
                statusCode.getForbiddenPostingStatus(post));
    }

    @PutMapping(value = "/{postId}")
    public ResponseEntity<Link> updatePost(
            @PathVariable long postId,
            @RequestBody Post post,
            @RequestHeader("Authorization") String auth
    ) {
        if (post.getTopic() != null ||
                post.getReceiverType() != null ||
                post.getReceiverId() != null) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        } else {
            Post updatedPost = postService.updateAPost(post, postId);
            return new ResponseEntity<>((updatedPost != null) ?
                    getPostLinkById(updatedPost.getId(), auth) : null,
                    statusCode.getBadRequestStatus(updatedPost));
        }
    }

    private ResponseEntity<List<Post>> getPostsToType(
            boolean receiverExist,
            String receiverType,
            String receiverId,
            String senderId,
            String search,
            Pageable page
    ) {
        return (!receiverExist) ?
                new ResponseEntity<>(null, HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(postService
                        .getPostsWithToAndFromId(
                                receiverType,
                                receiverId,
                                senderId,
                                search,
                                page),
                        HttpStatus.OK);
    }

    private Link getPostLinkById(long postId, String auth) {
        return linkTo(methodOn(PostController.class)
                .getPost(postId, auth))
                .withSelfRel();
    }

    private boolean receiverExists(String type, String id) {
        return switch (type.toLowerCase()) {
            case "group" -> groupService.groupExists(Long.parseLong(id));
            case "event" -> eventService.eventExists(Long.parseLong(id));
            case "user" -> userService.userExists(id);
            default -> false;
        };
    }

}
