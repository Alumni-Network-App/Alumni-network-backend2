package com.example.alumniserver.service;

import com.example.alumniserver.dao.PostRepository;
import com.example.alumniserver.dao.ReplyRepository;
import com.example.alumniserver.dao.RsvpRepository;
import com.example.alumniserver.dao.UserRepository;
import com.example.alumniserver.model.Reply;
import com.example.alumniserver.model.Rsvp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RsvpService {

    private final RsvpRepository rsvpRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostService postService;

    @Autowired
    public RsvpService(ReplyRepository repository, UserRepository userRepository, PostRepository postRepository, PostService postService, RsvpRepository rsvpRepository) {
        this.replyRepository = repository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postService = postService;
        this.rsvpRepository = rsvpRepository;
    }


    public List<Rsvp> getRsvps(Long eventId, String userId, Pageable page) {
        return rsvpRepository.getRsvps(eventId, userId, page).getContent();
    }
}
