package com.example.alumniserver.service;

import com.example.alumniserver.dao.TopicRepository;
import com.example.alumniserver.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {
    private final TopicRepository repository;

    @Autowired
    public TopicService(TopicRepository repository) {
        this.repository = repository;
    }

    public List<Topic> getAllTopics() {
        return repository.findAll();
    }

    public Topic getTopic(long id){
        return repository.findById(id);
    }

    public Topic createTopic(Topic topic){
        return repository.save(topic);
    }
}
