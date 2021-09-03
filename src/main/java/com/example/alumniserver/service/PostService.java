package com.example.alumniserver.service;

import com.example.alumniserver.dao.PostRepository;
import com.example.alumniserver.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository repository;

    @Autowired
    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> getAllPosts(long id) {
        return repository.findAllByUserId(id);
    }

}
