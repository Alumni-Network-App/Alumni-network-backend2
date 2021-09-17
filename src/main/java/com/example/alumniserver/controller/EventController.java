package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.idhelper.IdHelper;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Rsvp;
import com.example.alumniserver.service.EventService;
import com.example.alumniserver.service.GroupService;
import com.example.alumniserver.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping("/api/v1/event")
public class EventController {

    private final HttpStatusCode statusCode = new HttpStatusCode();
    private final EventService eventService;
    private final GroupService groupService;
    private final TopicService topicService;

    @Autowired
    public EventController(EventService eventService,
                           GroupService groupService,
                           TopicService topicService) {
        this.eventService = eventService;
        this.groupService = groupService;
        this.topicService = topicService;
    }

    @GetMapping(value = "/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable long eventId) {
        String userId = IdHelper.getLoggedInUserId();
        if (!eventService.eventExists(eventId))
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        else {
            Event event = eventService.getEvent(eventId);
            return (event.getUser().getId().equals(userId)) ?
                    new ResponseEntity<>(event, HttpStatus.OK) :
                    new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        String userId = IdHelper.getLoggedInUserId();
        List<Event> events = eventService.getAllUserEvents(userId);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        String userId = IdHelper.getLoggedInUserId();
        if(event.getNumberOfTopicInvites() == 0 && event.getNumberOfGroupInvites() == 0)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        event = eventService.createEvent(event, userId);
        return new ResponseEntity<>(event,
                statusCode.getForbiddenPostingStatus(event));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable long id,
                                            @RequestBody Event event) {
        String userId = IdHelper.getLoggedInUserId();
        if (!eventService.eventExists(id))
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        else {
            Event oldEvent = eventService.updateAnEvent(event, eventService.getEvent(id), userId);
            return new ResponseEntity<>(oldEvent,
                    statusCode.getForbiddenStatus(oldEvent != null));

        }
    }

    @PostMapping(value = "/{eventId}/invite/group/{groupId}")
    public ResponseEntity<Event> createGroupInvite(
            @PathVariable("eventId") long eventId,
            @PathVariable("groupId") long groupId) {
        String userId = IdHelper.getLoggedInUserId();

        if (eventService.eventExists(eventId)
                && groupService.groupExists(groupId)) {

            Event event = eventService.getEvent(eventId);
            if (!event.isGroupInvited(groupId)) {
                event = eventService.createEventInviteForGroup(userId, event, groupId);
                return new ResponseEntity<>(event,
                        statusCode.getForbiddenPostingStatus(event));
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{eventId}/invite/group/{groupId}")
    public ResponseEntity<Event> deleteGroupInvite(
            @PathVariable("eventId") long eventId,
            @PathVariable("groupId") long groupId) {
        String userId = IdHelper.getLoggedInUserId();

        if (eventService.eventExists(eventId)
                && groupService.groupExists(groupId)) {

            Event event = eventService.getEvent(eventId);
            if (event.isGroupInvited(groupId)) {
                event = eventService.deleteEventInviteForGroup(userId, event, groupId);
                return new ResponseEntity<>(
                        event, statusCode.getForbiddenStatus(event != null));
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/{eventId}/invite/topic/{topicId}")
    public ResponseEntity<Event> createEventTopicInvite(
            @PathVariable("eventId") long eventId,
            @PathVariable("topicId") long topicId) {
        String userId = IdHelper.getLoggedInUserId();

        if (eventService.eventExists(eventId)
                && topicService.topicExists(topicId)) {
            Event event = eventService.getEvent(eventId);
            if (!event.isTopicInvited(topicId)) {
                event = eventService.createEventTopicInvite(userId, event, topicId);
                return new ResponseEntity<>(event,
                        statusCode.getForbiddenPostingStatus(event));
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{eventId}/invite/topic/{topicId}")
    public ResponseEntity<Event> DeleteEventTopicInvite(
            @PathVariable("eventId") long eventId,
            @PathVariable("topicId") long topicId) {
        String userId = IdHelper.getLoggedInUserId();


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
        String userId = IdHelper.getLoggedInUserId();

        if (!eventService.eventExists(eventId))
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Event event = eventService.createUserInvite(userId,
                eventService.getEvent(eventId), invitedUserId);

        return new ResponseEntity<>(event,
                statusCode.getForbiddenPostingStatus(event));
    }

    @DeleteMapping(value = "/{eventId}/invite/user/{userId}")
    public ResponseEntity<Event> deleteEventInviteForUser(
            @PathVariable("eventId") long eventId,
            @PathVariable("userId") String invitedUserId) {
        String userId = IdHelper.getLoggedInUserId();

        if (!eventService.eventExists(eventId))
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Event event = eventService.deleteUserInvite(
                userId, eventService.getEvent(eventId), invitedUserId);

        return new ResponseEntity<>(event,
                statusCode.getForbiddenStatus(event != null));
    }

    @PostMapping(value = "/{eventId}/rsvp")
    public ResponseEntity<Rsvp> createRsvpRecord(
            @PathVariable("eventId") long eventId) {
        String userId = IdHelper.getLoggedInUserId();
        Event event = eventService.getEvent(eventId);

        if (event == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        Rsvp rsvp = eventService.createRsvpRecord(event, userId);
        return new ResponseEntity<>(rsvp, statusCode.getForbiddenPostingStatus(rsvp));
    }

}