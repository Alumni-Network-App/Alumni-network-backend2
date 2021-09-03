package com.example.alumniserver.controller;


import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Post;
import com.example.alumniserver.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/post")
public class PostController {

    private final HttpStatusCode statusCode = new HttpStatusCode();
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPostsForUser() {
        long id = new Long(1);
        List<Post> posts = postService.getAllPosts(id);
        return new ResponseEntity<>(posts, statusCode.getFoundStatus(posts));
    }

}
