package com.example.alumniserver.service;

import com.example.alumniserver.dao.EventRepository;
import com.example.alumniserver.dao.GroupRepository;
import com.example.alumniserver.dao.PostRepository;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository repository;
    private final GroupRepository groupRepository;
    private final EventRepository eventRepository;

    @Autowired
    public PostService(PostRepository repository, GroupRepository groupRepository, EventRepository eventRepository) {
        this.repository = repository;
        this.groupRepository = groupRepository;
        this.eventRepository = eventRepository;
    }

    public List<Post> getAllPosts(long id) {
        return repository.findAllByUserId(id);
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

    public boolean makeAPost(Post post, long senderId) {
        if(isPostingAllowed(post, senderId)) {
            repository.save(post);
            return true;
        } else {
            return false;
        }
    }

    private boolean isPostingAllowed(Post post, long senderId) {
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

}
