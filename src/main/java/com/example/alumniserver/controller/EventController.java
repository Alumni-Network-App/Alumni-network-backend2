package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.model.Topic;
import com.example.alumniserver.model.User;
import com.example.alumniserver.service.EventService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/api/v1/event")
public class EventController {

    private final HttpStatusCode statusCode = new HttpStatusCode();
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    //TODO Även topics som användaren är subscribad till ska returneras
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getAllTopics(@PathVariable long id){
        List<Event> events = eventService.getAllUserEvents(id);
        return new ResponseEntity<>(events, statusCode.getFoundStatus(events));
    }

    //TODO Måste fixa koll för att se om användaren är medlem i gruppen där eventet ska postas (true som standard nu)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Event> createEvent(@RequestBody Event event, Group group, User user){
        return new ResponseEntity<>(eventService.createEvent(event), statusCode.getForbiddenForGroupStatus(true, group.isPrivate()));
    }

    @Modifying
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Event> updateEvent(@RequestBody Event event, User user){
        return new ResponseEntity<>(eventService.createEvent(event), statusCode.getForbiddenForUpdateEventStatus(event, user));
    }

}
