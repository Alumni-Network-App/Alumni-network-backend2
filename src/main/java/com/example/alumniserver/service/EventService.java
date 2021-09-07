package com.example.alumniserver.service;

import com.example.alumniserver.dao.EventRepository;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository repository;

    @Autowired
    public EventService (EventRepository repository){
        this.repository = repository;

    }

    public List<Event> getAllUserEvents(long id) {
        return repository.findAllByUserId(id);
    }

    public Event createEvent(Event event){
        return repository.save(event);
    }


}
