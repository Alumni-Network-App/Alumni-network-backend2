package com.example.alumniserver.service;

import com.example.alumniserver.dao.TopicRepository;
import com.example.alumniserver.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository repository;

    @Autowired
    public TopicService(TopicRepository repository) {
        this.repository = repository;
    }

    public List<Topic> getTopics() {
        return repository.findAll();
    }

    public Topic getTopic(long topicId) {
        Optional<Topic> topic = repository.findById(topicId);
        return topic.orElse(null);
    }

    public Topic createTopic(Topic topic) {
        return (isValidTopic(topic)) ? repository.save(topic) : null;
    }

    public boolean topicExists(long topicId) {
        return repository.existsById(topicId);
    }

    private boolean isValidTopic(Topic topic) {
        return !topic.getName().equals("")
                && !topic.getDescription().equals("");
    }


}
