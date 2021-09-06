package com.example.alumniserver.controller;


import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Post;
import com.example.alumniserver.model.Reply;
import com.example.alumniserver.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/post")
public class PostController {

    private final HttpStatusCode statusCode = new HttpStatusCode();
    private final PostService postService;

    private static final long TEST_ID = 1;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPostsForUser() {
        long id = TEST_ID;
        List<Post> posts = postService.getAllPosts(id);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

    @GetMapping(value = "/user")
    public ResponseEntity<List<Post>> getPostsToUser() {
        long id = TEST_ID;
        List<Post> posts = postService.getPostsSentToUser("user", id);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<Post>> getPostsToUserFromUser(@PathVariable long userId) {
        long id = TEST_ID;
        List<Post> posts = postService.getPostsWithToAndFromId("user", id, userId);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

    @GetMapping(value = "/group/{groupId}")
    public ResponseEntity<List<Post>> getPostsToGroup(@PathVariable long groupId) {
        long id = TEST_ID;
        List<Post> posts = postService.getPostsWithToAndFromId("group", groupId, id);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

    @GetMapping(value = "/topic/{topicId}")
    public ResponseEntity<List<Post>> getPostsWithTopic(@PathVariable long topicId) {
        long id = TEST_ID;
        List<Post> posts = postService.getPostsFromUserToTopic(id, topicId);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

    @GetMapping(value = "/event/{eventId}")
    public ResponseEntity<List<Post>> getPostsToEvent(@PathVariable long eventId) {
        long id = TEST_ID;
        List<Post> posts = postService.getPostsWithToAndFromId("event", eventId, id);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

    @PostMapping
    public ResponseEntity<Boolean> createPost(@RequestBody Post post) {
        long id = TEST_ID;
        System.out.println(post);
        boolean posted = postService.makeAPost(post, id);
        return new ResponseEntity<>(posted, statusCode.getForbiddenStatus(posted));
    }

}
