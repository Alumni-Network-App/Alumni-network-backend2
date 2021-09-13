package com.example.alumniserver.service;

import com.example.alumniserver.dao.EventRepository;
import com.example.alumniserver.dao.GroupRepository;
import com.example.alumniserver.dao.PostRepository;
import com.example.alumniserver.dao.UserRepository;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.model.Post;
import com.example.alumniserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository repository;
    private final GroupRepository groupRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

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

    public List<Post> getPosts(String id, String receiverType, String search, Pageable pageable) {
        return (receiverType.equals(""))
                ? repository.getPosts(id, search, pageable).getContent()
                : repository.getFilteredPosts(id, receiverType, search, pageable).getContent();
    }

    public Post getPost(long postId) {
        return repository.findPostById(postId);
    }

    public boolean isUsersPost(String userId, Post post) {
        return post.getUser().getId().equals(userId);
    }

    public List<Post> getPostsSentToUser(String type, String id, String filter, Pageable page) {
        return repository.getFilteredPostsToTypeWithId(type, id, filter, page).getContent();
    }

    public List<Post> getPostsWithToAndFromId(String type, String receiverId, String senderId, String filter, Pageable page) {
        return repository.getFilteredPostsToTypeWithIdFromUser(type, receiverId, senderId, filter, page).getContent();
    }

    public List<Post> getPostsFromUserToTopic(String userId, long topicId, String filter, Pageable page) {
        return repository.getFilteredPostsToTopic(userId, topicId, filter, page).getContent();
    }

    public Post createPost(Post post, String senderId) {

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

    public Post updateAPost(Post post, long postId) {
        Post fetchedPost = repository.findPostById(postId);
        post.setId(postId);
        if(fetchedPost.getReceiverType().equals(post.getReceiverType())
                && postExists(postId)) {
            return repository.save(updateFields(post, fetchedPost));
        } else {
            return null;
        }
    }

    public boolean postExists(long postId) {
        return repository.existsById(postId);
    }

    public boolean isPostingAllowed(Post post, String senderId) {
        switch (post.getReceiverType()) {
            case "group":
                Group group = groupRepository.findGroupById(Long.parseLong(post.getReceiverId()));
                return !group.isPrivate() || group.isUserMember(senderId);
            case "event":
                Event event = eventRepository.findById(Long.parseLong(post.getReceiverId())).get();
                return event.isUserInvited(senderId);
            case "user":
                return true;
            default:
                return false;
        }
    }

    private User getUserInformation(String id) {
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
