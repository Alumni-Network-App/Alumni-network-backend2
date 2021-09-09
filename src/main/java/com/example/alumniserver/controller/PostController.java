package com.example.alumniserver.controller;


import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Post;
import com.example.alumniserver.model.Reply;
import com.example.alumniserver.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
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

    private static final String TEST_ID = "1";

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPostsForUser() {
        String id = TEST_ID;
        List<Post> posts = postService.getAllPosts(id);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable long postId) {
        String id = TEST_ID;
        return postService.getPost(postId, id);
    }

    @GetMapping(value = "/user")
    public ResponseEntity<List<Post>> getPostsToUser() {
        String id = TEST_ID;
        List<Post> posts = postService.getPostsSentToUser("user", id);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<Post>> getPostsToUserFromUser(@PathVariable String userId) {
        String id = TEST_ID;
        List<Post> posts = postService.getPostsWithToAndFromId("user", id, userId);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

    @GetMapping(value = "/group/{groupId}")
    public ResponseEntity<List<Post>> getPostsToGroup(@PathVariable long groupId) {
        String id = TEST_ID;

        List<Post> posts = postService.getPostsWithToAndFromId("group", String.valueOf(groupId), id);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

    @GetMapping(value = "/topic/{topicId}")
    public ResponseEntity<List<Post>> getPostsWithTopic(@PathVariable long topicId) {
        String id = TEST_ID;
        List<Post> posts = postService.getPostsFromUserToTopic(id, topicId);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

    @GetMapping(value = "/event/{eventId}")
    public ResponseEntity<List<Post>> getPostsToEvent(@PathVariable long eventId) {
        String id = TEST_ID;
        List<Post> posts = postService.getPostsWithToAndFromId("event", String.valueOf(eventId), id);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

    @PostMapping
    public ResponseEntity<Link> createPost(@RequestBody Post post) {
        String id = TEST_ID;
        post = postService.makeAPost(post, id);
        return new ResponseEntity<>(getPostLinkById(post.getId()),
                statusCode.getForbiddenPostingStatus(post));
    }

    @PutMapping(value = "/{postId}")
    public ResponseEntity<Link> updatePost(
            @PathVariable long postId,
            @RequestBody Post post
    ) {
        Post updatedPost = postService.updateAPost(post, postId);
        return new ResponseEntity<>(getPostLinkById(updatedPost.getId()),
                statusCode.getForbiddenStatus(updatedPost == null));
    }

    private Link getPostLinkById(long postId) {
        return linkTo(methodOn(PostController.class)
                .getPost(postId))
                .withSelfRel();
    }

}
