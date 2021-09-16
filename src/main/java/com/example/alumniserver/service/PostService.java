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
    private final GroupService groupService;
    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public PostService(
            PostRepository repository,
            GroupService groupService,
            EventService eventService,
            UserService userService
    ) {
        this.repository = repository;
        this.groupService = groupService;
        this.eventService = eventService;
        this.userService = userService;
    }

    public List<Post> getPosts(String id, String receiverType, String search, Pageable pageable) {
        return (receiverType.equals(""))
                ? repository.getPosts(id, search, pageable).getContent()
                : repository.getFilteredPosts(id, receiverType, search, pageable).getContent();
    }

    public Post getPost(long postId) {
        return repository.findPostByIdOrderByDateDesc(postId);
    }

    public boolean isUsersPost(String userId, Post post) {
        return post.getUser().getId().equals(userId);
    }

    public List<Post> getPostsSentToUser(String type, String receiverId, String userId, String filter, Pageable page) {
        return repository.getFilteredPostsToTypeWithId(type, receiverId, userId, filter, page).getContent();
    }

    public List<Post> getPostsWithToAndFromId(String type, String receiverId, String senderId, String filter, Pageable page) {
        return (isFetchAllowed(type, receiverId, senderId)) ?
                repository
                        .getFilteredPostsToTypeWithIdFromUser(type, receiverId, filter, page)
                        .getContent() : null;
    }

    public List<Post> getPostsFromUserToTopic(long topicId, String userId, String filter, Pageable page) {
        return repository.getFilteredPostsToTopic(topicId, userId, filter, page).getContent();
    }

    public Post createPost(Post post, String senderId) {

        if (isPostingAllowed(post, senderId)) {
            User user = getUserInformation(senderId);
            post.setUser(user);
            user.addPost(post);
            post = repository.save(post);
            userService.updateUserRelations(user);
            return post;
        } else {
            return null;
        }
    }

    public Post updateAPost(Post post, long postId) {
        Post fetchedPost = repository.findPostByIdOrderByDateDesc(postId);
        post.setId(postId);
        if (fetchedPost.getReceiverType().equals(post.getReceiverType())
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
                Group group = groupService.getGroup(Long.parseLong(post.getReceiverId()));
                return !group.isPrivate() || group.isUserMember(senderId);
            case "event":
                Event event = eventService.getEvent(Long.parseLong(post.getReceiverId()));
                return event.isUserInvited(senderId);
            case "user":
                return true;
            default:
                return false;
        }
    }

    private boolean isFetchAllowed(String receiverType, String receiverId, String senderId) {
        return switch (receiverType) {
            case "group" -> groupService.getGroup(Long.parseLong(receiverId)).isUserMember(senderId);
            case "event" -> eventService.getEvent(Long.parseLong(receiverId)).isUserInvited(senderId);
            case "user" -> true;
            default -> false;
        };
    }

    private User getUserInformation(String id) {
        return userService.getUserById(id);
    }

    private Post updateFields(Post updatedPost, Post oldPost) {
        if (!updatedPost.getTitle().equals(""))
            oldPost.setTitle(updatedPost.getTitle());
        if (!updatedPost.getContent().equals(""))
            oldPost.setContent(updatedPost.getContent());
        oldPost.setDate(updatedPost.getDate());
        return oldPost;
    }

}
