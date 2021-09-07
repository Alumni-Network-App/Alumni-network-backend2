package com.example.alumniserver.service;

import com.example.alumniserver.dao.EventRepository;
import com.example.alumniserver.model.Event;
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

    public Event getEvent(long eventId) {
        return repository.findEventByID(eventId);
    }

    public boolean updateAnEvent(Event event, long postId, long userId) {
        Event fetchedEvent = repository.getById(postId);
        event.setId(postId);
        if(fetchedEvent.getId() == event.getId() && checkIfUserAllowedToUpdate(event, userId)) {
            repository.save(updateFields(event, fetchedEvent));
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfUserAllowedToUpdate(Event event, long userId){
        if(event.getUser().getId()==userId){
            return true;
        }else{
            return false;
        }
    }

    private Event updateFields(Event updatedEvent, Event oldEvent) {
        if(!updatedEvent.getName().equals(""))
            oldEvent.setName(updatedEvent.getName());

        if(!updatedEvent.getDescription().equals(""))
            oldEvent.setDescription(updatedEvent.getDescription());

        if(!updatedEvent.getStartTime().equals(null) && !updatedEvent.getEndTime().equals(null))
            oldEvent.setStartTime(updatedEvent.getStartTime());
            oldEvent.setEndTime(updatedEvent.getEndTime());

        if (!updatedEvent.getBannerImg().equals(""))
            oldEvent.setBannerImg(updatedEvent.getBannerImg());

        return oldEvent;

    }

}
