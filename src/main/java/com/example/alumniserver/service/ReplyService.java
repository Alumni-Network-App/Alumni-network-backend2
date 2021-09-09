package com.example.alumniserver.service;

import com.example.alumniserver.dao.*;
import com.example.alumniserver.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {

    private final ReplyRepository repository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostService postService;

    @Autowired
    public ReplyService(ReplyRepository repository,
                        UserRepository userRepository,
                        PostRepository postRepository,
                        PostService postService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postService = postService;
    }

    public Reply getReplyWithId(long replyId) {
        return repository.findReplyById(replyId);
    }

    public List<Reply> getRepliesWithUserId(String userId) {
        return repository.findRepliesByUserId(userId);
    }

    public List<Reply> getRepliesToPostId(long postId) {
        return repository.findAllByPostId(postId);
    }

    public Reply createReply(Reply reply, long postId, String userId) {
        Post post = getPostInformation(postId);
        if (postService.isPostingAllowed(post, userId)) {
            return updateReplyRelations(reply, post, userId);
        } else {
            return null;
        }
    }

    //Check if logged in user is reply "owner"  if not return null.
    public Reply updateReply(Reply reply, long replyId) {
        Reply fetchedReply = repository.findReplyById(replyId);
        reply.setId(replyId);
        return repository.save(updateFields(reply, fetchedReply));
    }

    private Reply updateFields(Reply updatedReply, Reply oldReply) {
        if (!updatedReply.getContent().equals(""))
            oldReply.setContent(updatedReply.getContent());
        oldReply.setLastUpdated(oldReply.getLastUpdated());
        return oldReply;
    }

    private Reply updateReplyRelations(Reply reply, Post post, String userId) {
        Post updatedPost = updatePostRelations(reply, post);
        User user = updateUserRelations(reply, userId);
        reply.setPost(updatedPost);
        reply.setUser(user);
        return repository.save(reply);
    }

    private User updateUserRelations(Reply reply, String userId) {
        User user = getUserInformation(userId);
        user.addReply(reply);
        userRepository.save(user);
        return user;
    }

    private Post updatePostRelations(Reply reply, Post post) {
        post.addReply(reply);
        postRepository.save(post);
        return post;
    }

    private User getUserInformation(String userId) {
        return userRepository.findUserById(userId);
    }

    private Post getPostInformation(long postId) {
        return postRepository.findPostById(postId);
    }


}
