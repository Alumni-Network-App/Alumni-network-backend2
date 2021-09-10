package com.example.alumniserver.controller;


import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Post;
import com.example.alumniserver.model.Reply;
import com.example.alumniserver.model.User;
import com.example.alumniserver.service.*;
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
@RequestMapping("/api/v1/post")
public class PostController {

    private final HttpStatusCode statusCode = new HttpStatusCode();
    private final PostService postService;
    private final UserService userService;
    private final GroupService groupService;
    private final TopicService topicService;
    private final EventService eventService;

    private static final String TEST_ID = "1";

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
    public ResponseEntity<List<Post>> getPostsForUser() {
        String id = TEST_ID;
        List<Post> posts = postService.getAllPosts(id);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable long postId) {
        String id = TEST_ID;

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
    public ResponseEntity<List<Post>> getPostsToUser() {
        String id = TEST_ID;
        List<Post> posts = postService.getPostsSentToUser("user", id);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<Post>> getPostsToUserFromUser(@PathVariable String userId) {
        String id = TEST_ID;
        boolean userFound = userService.userExists(userId);
        return getPostsToType(userFound, "user", id, userId);
    }

    @GetMapping(value = "/group/{groupId}")
    public ResponseEntity<List<Post>> getPostsToGroup(@PathVariable String groupId) {
        String id = TEST_ID;
        boolean groupFound = groupService.groupExists(Long.valueOf(groupId));
        return getPostsToType(groupFound, "group", groupId, id);
    }

    @GetMapping(value = "/topic/{topicId}")
    public ResponseEntity<List<Post>> getPostsWithTopic(@PathVariable long topicId) {
        String id = TEST_ID;
        boolean topicExists = topicService.topicExists(topicId);
        return (!topicExists) ?
                new ResponseEntity<>(null, HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(postService
                        .getPostsFromUserToTopic(id, topicId),
                        HttpStatus.OK);
    }

    @GetMapping(value = "/event/{eventId}")
    public ResponseEntity<List<Post>> getPostsToEvent(@PathVariable String eventId) {
        String id = TEST_ID;
        boolean eventExists = eventService.eventExists(Long.valueOf(eventId));
        return getPostsToType(eventExists, "event", eventId, id);
    }

    @PostMapping
    public ResponseEntity<Link> createPost(@RequestBody Post post) {
        String id = TEST_ID;
        if(post.getReceiverType() == null
                || post.getReceiverId() == null
                || post.getTopic() == null
                || !topicService.topicExists(post.getTopic().getId())
        ) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        post = postService.makeAPost(post, id);
        return new ResponseEntity<>(getPostLinkById(post.getId()),
                statusCode.getForbiddenPostingStatus(post));
    }

    @PutMapping(value = "/{postId}")
    public ResponseEntity<Link> updatePost(
            @PathVariable long postId,
            @RequestBody Post post
    ) {
        if(post.getTopic() != null ||
                post.getReceiverType() != null ||
                post.getReceiverId() != null
        ) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        } else {
            Post updatedPost = postService.updateAPost(post, postId);
            return new ResponseEntity<>((updatedPost != null) ?
                    getPostLinkById(updatedPost.getId()) : null,
                    statusCode.getBadRequestStatus(updatedPost));
        }
    }

    private ResponseEntity<List<Post>> getPostsToType(
            boolean receiverExist,
            String receiverType,
            String receiverId,
            String senderId
    ) {
        return (!receiverExist) ?
                new ResponseEntity<>(null, HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(postService
                        .getPostsWithToAndFromId(
                                receiverType,
                                receiverId,
                                senderId),
                        HttpStatus.OK);
    }

    private Link getPostLinkById(long postId) {
        return linkTo(methodOn(PostController.class)
                .getPost(postId))
                .withSelfRel();
    }

}
