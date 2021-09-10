package com.example.alumniserver.service;

import com.example.alumniserver.dao.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public boolean eventExists(long eventId) {
        return repository.existsById(eventId);
    }

}
