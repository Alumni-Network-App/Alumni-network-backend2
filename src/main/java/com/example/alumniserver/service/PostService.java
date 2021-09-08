package com.example.alumniserver.service;

import com.example.alumniserver.dao.EventRepository;
import com.example.alumniserver.dao.GroupRepository;
import com.example.alumniserver.dao.PostRepository;
import com.example.alumniserver.dao.UserRepository;
import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.model.Post;
import com.example.alumniserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository repository;
    private final GroupRepository groupRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final HttpStatusCode statusCode = new HttpStatusCode();

    @Autowired
    public PostService(
            PostRepository repository,
            GroupRepository groupRepository,
            EventRepository eventRepository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.groupRepository = groupRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<Post> getAllPosts(long id) {
        return repository.findAllByUserId(id);
    }

    public ResponseEntity<Post> getPost(long postId, long userId) {
        Post post = repository.findPostById(postId);
        HttpStatus foundCode = statusCode.getFoundStatus(post);
         if(foundCode == HttpStatus.NOT_FOUND || isPostingAllowed(post, userId)) {
            return new ResponseEntity<>(post, foundCode);
        } else {
            return new ResponseEntity<>(null, statusCode.getForbiddenStatus(false));
        }
    }

    public List<Post> getPostsSentToUser(String type, long id) {
        return repository.findAllByReceiverTypeAndReceiverId(type, id);
    }

    public List<Post> getPostsWithToAndFromId(String type, long receiverId, long senderId) {
        return repository.findAllByReceiverTypeAndReceiverIdAndUserId(type, receiverId, senderId);
    }

    public List<Post> getPostsFromUserToTopic(long userId, long topicId) {
        return repository.findAllByUserIdAndTopicId(userId, topicId);
    }

    public Post makeAPost(Post post, long senderId) {

        if(isPostingAllowed(post, senderId)) {
            User user = getUserInformation(senderId);
            post.setUser(user);
            user.addPost(post);
            post = repository.save(post);
            userRepository.save(user);
            return post;
        } else {
            return null;
        }
    }

    public boolean updateAPost(Post post, long postId) {
        Post fetchedPost = repository.findPostById(postId);
        post.setId(postId);
        if(fetchedPost.getReceiverType().equals(post.getReceiverType())) {
            repository.save(updateFields(post, fetchedPost));
            return true;
        } else {
            return false;
        }
    }

    public boolean isPostingAllowed(Post post, long senderId) {
        switch (post.getReceiverType()) {
            case "group":
                Group group = groupRepository.getById(post.getReceiverId());
                return (group.isPrivate() && !group.isUserMember(senderId)) ? false : true;
            case "event":
                Event event = eventRepository.getById(post.getReceiverId());
                return (event.isUserInvited(senderId)) ? true : false;
            case "user":
                return true;
            default:
                return false;
        }
    }

    private User getUserInformation(long id) {
        return userRepository.findUserById(id);
    }

    private Post updateFields(Post updatedPost, Post oldPost) {
        if(!updatedPost.getTitle().equals(""))
            oldPost.setTitle(updatedPost.getTitle());
        if(!updatedPost.getContent().equals(""))
            oldPost.setContent(updatedPost.getContent());
        oldPost.setDate(updatedPost.getDate());
        return oldPost;
    }

}
