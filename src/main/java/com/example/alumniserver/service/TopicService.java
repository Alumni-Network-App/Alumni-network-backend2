package com.example.alumniserver.service;

import com.example.alumniserver.dao.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

    private final TopicRepository repository;

    @Autowired
    public TopicService(TopicRepository repository) {
        this.repository = repository;
    }

    public boolean topicExists(long topicId) {
        return repository.existsById(topicId);
    }

}
