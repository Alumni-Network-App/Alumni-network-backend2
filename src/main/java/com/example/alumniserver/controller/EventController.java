package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {

    private final HttpStatusCode statusCode = new HttpStatusCode();
    private final EventService eventService;
    private static final long TEST_ID = 1;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    //TODO Även topics som användaren är subscribad till ska returneras
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getAllEvents(){
        long userId = TEST_ID;
        List<Event> events = eventService.getAllUserEvents(userId);
        return new ResponseEntity<>(events, statusCode.getFoundStatus(events));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Event> createEvent(@RequestBody Event event, Group group){
        long userId = TEST_ID;
        return new ResponseEntity<>(eventService.createEvent(event), statusCode.getForbiddenStatus(group.isUserMember(userId)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> updateEvent(@PathVariable("id") long id, @RequestBody Event events){
        long userId = TEST_ID;
        Event event = eventService.getEvent(id);
        Boolean updated = eventService.updateAnEvent(events, event.getId(), userId);
        HttpStatus httpStatus = (statusCode.getFoundStatus(event) == HttpStatus.NOT_FOUND)
                ? httpStatus = HttpStatus.NOT_FOUND : statusCode.getForbiddenToUpdateEventStatus(event.isUserCreator(userId));

        return new ResponseEntity<>(updated, httpStatus);
    }

    @RequestMapping(value = "/{eventId}/invite/group/{groupId}", method = RequestMethod.POST)
    public ResponseEntity<Event> createGroupInvite(@PathVariable("eventId") long eventId, @PathVariable("groupId") long groupId){
        boolean checkIfAdded;
        long userId = TEST_ID;

        Event event = eventService.getEvent(eventId);
        if(event == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        if(!event.isUserCreator(userId))
            return new ResponseEntity<>(event, HttpStatus.FORBIDDEN);
        else
            checkIfAdded = eventService.createEventInviteForGroup(event, groupId);

        return new ResponseEntity<>(event, statusCode.getForbiddenStatus(checkIfAdded));
    }

    @RequestMapping(value = "/{eventId}/invite/group/{groupId}", method = RequestMethod.DELETE)
    public ResponseEntity<Event> deleteGroupInvite(@PathVariable("eventId") long eventId, @PathVariable("groupId") long groupId){
        boolean checkIfDeleted;
        long userId = TEST_ID;

        Event event = eventService.getEvent(eventId);
        if(event == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        if(!event.isUserCreator(userId))
            return new ResponseEntity<>(event, HttpStatus.FORBIDDEN);
        else
            checkIfDeleted = eventService.deleteEventInviteForGroup(event, groupId);

        return new ResponseEntity<>(event, statusCode.getForbiddenStatus(checkIfDeleted));
    }

    @RequestMapping(value = "/{eventId}/invite/topic/{topicId}", method = RequestMethod.POST)
    public ResponseEntity<Event> createEventTopicInvite(@PathVariable("eventId") long eventId, @PathVariable("topicId") long topicId){
        boolean checkIfCreated;
        long userId = TEST_ID;

        Event event = eventService.getEvent(eventId);
        if(event == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        if(!event.isUserCreator(userId))
            return new ResponseEntity<>(event, HttpStatus.FORBIDDEN);
        else
            checkIfCreated = eventService.createEventTopicInvite(event, topicId);

        return new ResponseEntity<>(event, statusCode.getForbiddenStatus(checkIfCreated));
    }

    @RequestMapping(value = "/{eventId}/invite/topic/{topicId}", method = RequestMethod.DELETE)
    public ResponseEntity<Event> DeleteEventTopicInvite(@PathVariable("eventId") long eventId, @PathVariable("topicId") long topicId){
        boolean checkIfDeleted;
        long userId = TEST_ID;

        Event event = eventService.getEvent(topicId);
        if(event == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        if(!event.isUserCreator(userId))
            return new ResponseEntity<>(event, HttpStatus.FORBIDDEN);

        else
            checkIfDeleted = eventService.deleteEventTopicInvite(event, topicId);

        return new ResponseEntity<>(event, statusCode.getForbiddenStatus(checkIfDeleted));
    }

    @RequestMapping(value = "/{eventId}/invite/user/{userId}", method = RequestMethod.POST)
    public ResponseEntity<Event> createEventInviteForUser(@PathVariable("eventId") long eventId, @PathVariable("invitedUserId") long invitedUserId){
        boolean checkIfCreated;
        long userId = TEST_ID;

        Event event = eventService.getEvent(eventId);
        if(event == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        if(!event.isUserCreator(userId))
            return new ResponseEntity<>(event, HttpStatus.FORBIDDEN);
        else
            checkIfCreated = eventService.createUserInvite(event, invitedUserId);

        return new ResponseEntity<>(event, statusCode.getForbiddenStatus(checkIfCreated));
    }

    @RequestMapping(value = "/{eventId}/invite/user/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Event> deleteEventInviteForUser(@PathVariable("eventId") long eventId, @PathVariable("invitedUserId") long invitedUserId){
        boolean checkIfDeleted;
        long userId = TEST_ID;

        Event event = eventService.getEvent(eventId);
        if(event == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        if(!event.isUserCreator(userId))
            return new ResponseEntity<>(event, HttpStatus.FORBIDDEN);
        else
            checkIfDeleted = eventService.deleteUserInvite(event, invitedUserId);

        return new ResponseEntity<>(event, statusCode.getForbiddenStatus(checkIfDeleted));
    }

    //TODO fixa denna sen när tabellen är klar
    //POST /event/:event_id/rsvp
    //Create a new event rsvp record. Accepts appropriate parameters in the request body
    //as application/json. By default, user_id is taken as being that of the requesting user
    //and event_id is provided in the path.
    //If the requesting user is not part of an invited group or topic, or has not been invited
    //individually, the request will result in a 403 Forbidden response.
    @RequestMapping(value = "/{eventId}/rsvp", method = RequestMethod.POST)
    public ResponseEntity<Event> createRsvpRecord(@PathVariable("eventId") long eventId){
        boolean checkIfCreated;
        long userId = TEST_ID;
        Event event = eventService.getEvent(eventId);

        if(event == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);


    }


}
