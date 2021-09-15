package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.model.Topic;
import com.example.alumniserver.service.EventService;
import com.example.alumniserver.service.GroupService;
import com.example.alumniserver.service.TopicService;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {

    private final HttpStatusCode statusCode = new HttpStatusCode();
    private final EventService eventService;
    private final GroupService groupService;
    private final TopicService topicService;
    private static final String TEST_ID = "3";

    public EventController(EventService eventService,
                           GroupService groupService,
                           TopicService topicService) {
        this.eventService = eventService;
        this.groupService = groupService;
        this.topicService = topicService;
    }

    @GetMapping(value = "/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable long eventId) {
        String userId = TEST_ID;
        if (!eventService.eventExists(eventId))
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        else {
            Event event = eventService.getEvent(eventId);
            return (event.getUser().getId().equals(userId)) ?
                    new ResponseEntity<>(event, HttpStatus.OK) :
                    new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

    }

    //TODO Även topics som användaren är subscribad till ska returneras
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        String userId = TEST_ID;
        List<Event> events = eventService.getAllUserEvents(userId);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    //Behövs uppdateras, ta bort group då det kan postas i event, och kolla att group / topic är satt.
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        String userId = TEST_ID;
        event = eventService.createEvent(event, userId);
        return new ResponseEntity<>(event,
                statusCode.getForbiddenStatus(event != null));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Link> updateEvent(@PathVariable long id,
                                            @RequestBody Event event) {
        String userId = TEST_ID;
        if (!eventService.eventExists(id))
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        else {
            Event oldEvent = eventService.updateAnEvent(event, eventService.getEvent(id), userId);
            return new ResponseEntity<>(getEventLinkById(oldEvent),
                    statusCode.getForbiddenStatus(oldEvent != null));

        }
    }

    @PostMapping(value = "/{eventId}/invite/group/{groupId}")
    public ResponseEntity<Link> createGroupInvite(
            @PathVariable("eventId") long eventId,
            @PathVariable("groupId") long groupId) {
        String userId = TEST_ID;

        if (eventService.eventExists(eventId)
                && groupService.groupExists(groupId)) {

            Event event = eventService.getEvent(eventId);
            if (!event.isGroupInvited(groupId)) {
                event = eventService.createEventInviteForGroup(userId, event, groupId);
                return new ResponseEntity<>(getEventLinkById(event),
                        statusCode.getForbiddenStatus(event != null));
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{eventId}/invite/group/{groupId}")
    public ResponseEntity<Event> deleteGroupInvite(
            @PathVariable("eventId") long eventId,
            @PathVariable("groupId") long groupId) {
        String userId = TEST_ID;

        if (eventService.eventExists(eventId)
                && groupService.groupExists(groupId)) {

            Event event = eventService.getEvent(eventId);
            if (event.isGroupInvited(groupId)) {
                event = eventService.deleteEventInviteForGroup(userId, event, groupId);
                return new ResponseEntity<>(event, statusCode.getForbiddenStatus(event != null));
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{eventId}/invite/topic/{topicId}")
    public ResponseEntity<Link> createEventTopicInvite(
            @PathVariable("eventId") long eventId,
            @PathVariable("topicId") long topicId) {
        String userId = TEST_ID;

        if (eventService.eventExists(eventId)
                && topicService.topicExists(topicId)) {
            Event event = eventService.getEvent(eventId);
            if (!event.isTopicInvited(topicId)) {
                event = eventService.createEventTopicInvite(userId, event, topicId);
                return new ResponseEntity<>(getEventLinkById(event),
                        statusCode.getForbiddenStatus(event != null));
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{eventId}/invite/topic/{topicId}")
    public ResponseEntity<Event> DeleteEventTopicInvite(
            @PathVariable("eventId") long eventId,
            @PathVariable("topicId") long topicId) {
        String userId = TEST_ID;


        if (eventService.eventExists(eventId)
                && groupService.groupExists(topicId)) {
            Event event = eventService.getEvent(eventId);
            if (event.isTopicInvited(topicId)) {
                event = eventService.deleteEventTopicInvite(userId, event, topicId);
                return new ResponseEntity<>(event, statusCode.getForbiddenStatus(event != null));
            }
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{eventId}/invite/user/{userId}")
    public ResponseEntity<Event> createEventInviteForUser(
            @PathVariable("eventId") long eventId,
            @PathVariable("userId") String invitedUserId) {
        String userId = TEST_ID;

        if (!eventService.eventExists(eventId))
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Event event = eventService.createUserInvite(userId,
                eventService.getEvent(eventId), invitedUserId);

        return new ResponseEntity<>(event,
                statusCode.getForbiddenStatus(event != null));
    }

    @DeleteMapping(value = "/{eventId}/invite/user/{userId}")
    public ResponseEntity<Event> deleteEventInviteForUser(
            @PathVariable("eventId") long eventId,
            @PathVariable("userId") String invitedUserId) {
        String userId = TEST_ID;

        if (!eventService.eventExists(eventId))
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Event event = eventService.deleteUserInvite(
                userId, eventService.getEvent(eventId), invitedUserId);

        return new ResponseEntity<>(event,
                statusCode.getForbiddenStatus(event != null));
    }

    //TODO fixa denna sen när tabellen är klar
    //POST /event/:event_id/rsvp
    //Create a new event rsvp record. Accepts appropriate parameters in the request body
    //as application/json. By default, user_id is taken as being that of the requesting user
    //and event_id is provided in the path.
    //If the requesting user is not part of an invited group or topic, or has not been invited
    //individually, the request will result in a 403 Forbidden response.
    @PostMapping(value = "/{eventId}/rsvp")
    public ResponseEntity<Event> createRsvpRecord(
            @PathVariable("eventId") long eventId,
            @RequestBody Group group, Topic topic) {
        boolean checkIfCreated;
        String userId = TEST_ID;
        Event event = eventService.getEvent(eventId);

        if (event == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        checkIfCreated = eventService.createRsvpRecord(event, group, event.getTopic(), userId);
        return new ResponseEntity<>(event, statusCode.getForbiddenStatus(checkIfCreated));
    }

    private Link getEventLinkById(Event event) {
        if (event == null)
            return null;
        return linkTo(methodOn(EventController.class)
                .getEvent(event.getId()))
                .withSelfRel();
    }

}