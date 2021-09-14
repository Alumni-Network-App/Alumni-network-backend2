package com.example.alumniserver.service;

import com.example.alumniserver.dao.TopicRepository;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Topic;
import com.example.alumniserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository repository;
    private final UserService userService;

    @Autowired
    public TopicService(TopicRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public List<Topic> getTopics(Pageable page, String name) {
        return repository.findTopics(name, page).getContent();
    }

    public Topic getTopic(long topicId) {
        Optional<Topic> topic = repository.findById(topicId);
        return topic.orElse(null);
    }

    public Topic createTopic(Topic topic) {
        return (isValidTopic(topic)) ? repository.save(topic) : null;
    }

    public Topic createTopicSubscription(Topic topic, User user) {
        return repository.save(updateTopicRelationsWithUser(topic, user));
    }

    public boolean topicExists(long topicId) {
        return repository.existsById(topicId);
    }

    private Topic updateTopicRelationsWithUser(Topic topic, User user) {
        topic.addUserToTopic(user);
        user.addTopicToSubscription(topic);
        return topic;
    }

    private boolean isValidTopic(Topic topic) {
        return !topic.getName().equals("")
                && !topic.getDescription().equals("");
    }

    public Topic addEventToTopic(Event event, Topic topic) {
        return (topic.addEventToTopic(event)) ? repository.save(topic) : null;
    }

    public Topic deleteEventFromTopic(Event event, Topic topic) {
        return (topic != null && topic.deleteEventFromTopic(event))
                ? repository.save(topic) : null;
    }
}
