package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.model.Topic;
import com.example.alumniserver.model.User;
import com.example.alumniserver.service.EventService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {

    private final HttpStatusCode statusCode = new HttpStatusCode();
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    //TODO Även topics som användaren är subscribad till ska returneras
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getAllEvents(@RequestBody long id){
        List<Event> events = eventService.getAllUserEvents(id);
        return new ResponseEntity<>(events, statusCode.getFoundStatus(events));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Event> createEvent(@RequestBody Event event, Group group, long id){
        return new ResponseEntity<>(eventService.createEvent(event), statusCode.getForbiddenStatus(group.isUserMember(id)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> updateEvent(@PathVariable("id") long id, @RequestBody long userId, Event events){
        Event event = eventService.getEvent(id);
        Boolean updated = eventService.updateAnEvent(events, event.getId(), userId);
        HttpStatus httpStatus = (statusCode.getFoundStatus(event) == HttpStatus.NOT_FOUND)
                ? httpStatus = HttpStatus.NOT_FOUND : statusCode.getForbiddenToUpdateEventStatus(event.isUserCreator(userId));

        return new ResponseEntity<>(updated, httpStatus);
    }

    //TODO fixa denna
    //POST /event/:event_id/invite/group/:group_id
    //Create a new event group invitation for the event and group specified in the path.
    //Accepts no parameters.
    //Only the event creator may create event invitations. Requests to do so from all other
    //users will result in a 403 Forbidden response.
    @RequestMapping(value = "/{eventId}/invite/group/{groupId}", method = RequestMethod.POST)
    public ResponseEntity<Boolean> sendGroupInvite(@PathVariable("eventId") long eventId, @PathVariable("groupId") long groupId){
        Boolean hello = false;
        return new ResponseEntity<>(hello, statusCode.getContentStatus());
    }

    //TODO fixa denna
    //DELETE /event/:event_id/invite/group/:group_id
    //Remove an existing event invitation for the event and group specified in the path.
    //Accepts no parameters.
    //Only the event creator may create event invitations. Requests to do so from all other
    //users will result in a 403 Forbidden response.
    //Removal of the event invitation does not automatically remove RSVP records that
    //were authorized by that invitation before it was removed.
    public void removeGroupInvite(){

    }


}
