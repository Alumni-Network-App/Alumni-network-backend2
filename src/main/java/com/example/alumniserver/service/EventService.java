package com.example.alumniserver.service;

import com.example.alumniserver.dao.EventRepository;
import com.example.alumniserver.model.Event;
import com.example.alumniserver.model.Group;
import com.example.alumniserver.model.Topic;
import com.example.alumniserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository repository;
    private final UserService userService;
    private final GroupService groupService;
    private final TopicService topicService;

    @Autowired
    public EventService(EventRepository repository,
                        UserService userService,
                        GroupService groupService,
                        TopicService topicService) {
        this.repository = repository;
        this.userService = userService;
        this.groupService = groupService;
        this.topicService = topicService;
    }

    public boolean eventExists(long eventId) {
        return repository.existsById(eventId);
    }

    public List<Event> getAllUserEvents(String id) {
        return repository.findAllByUserId(id);
    }

    public Event createEvent(Event event) {
        return repository.save(event);
    }

    public Event getEvent(long eventId) {
        return repository.findEventById(eventId);
    }

    public Event updateAnEvent(Event newEvent, Event oldEvent, String userId) {
        return (!oldEvent.isUserCreator(userId))
                ? repository.save(updateFields(newEvent, oldEvent)) : null;
    }

    private Event updateFields(Event updatedEvent, Event oldEvent) {
        if (updatedEvent.getName() != null)
            oldEvent.setName(updatedEvent.getName());

        if (updatedEvent.getDescription() != null)
            oldEvent.setDescription(updatedEvent.getDescription());

        if (updatedEvent.getStartTime() != null)
            oldEvent.setStartTime(updatedEvent.getStartTime());

        if (updatedEvent.getEndTime() != null)
            oldEvent.setEndTime(updatedEvent.getEndTime());

        if (updatedEvent.getBannerImg() != null)
            oldEvent.setBannerImg(updatedEvent.getBannerImg());

        return oldEvent;

    }

    public Event createEventInviteForGroup(String userId, Event event, long groupId) {
        if (!event.isUserCreator(userId))
            return null;
        Group group = groupService.getGroup(groupId);
        if (event.inviteGroup(group, userId)) {
            groupService.addEventToGroup(event, group);
            return repository.save(event);
        } else
            return null;
    }

    public Event deleteEventInviteForGroup(String userId, Event event, long groupId) {
        if (!event.isUserCreator(userId))
            return null;
        Group group = groupService.getGroup(groupId);
        if (event.deleteGroupInvite(group)) {
            groupService.removeEventFromGroup(event, group);
            return repository.save(event);
        } else {
            return null;
        }
    }

    public Event createEventTopicInvite(String userId, Event event, long topicId) {
        if (!event.isUserCreator(userId))
            return null;
        Topic topic = topicService.getTopic(topicId);
        return (event.setInviteTopic(topic)) ?
                repository.save(event) : null;
    }

    public Event deleteEventTopicInvite(String userId, Event event, long topicId) {
        if (!event.isUserCreator(userId))
            return null;
        Topic topic = topicService.getTopic(topicId);
        return (event.deleteInviteTopic(topic)) ?
                repository.save(event) : null;
    }

    public Event createUserInvite(String creatorId, Event event, String userId) {
        if (!event.isUserCreator(creatorId))
            return null;
        User user = userService.getUserById(userId);
        return (event.setUserInvite(user)) ?
                repository.save(event) : null;
    }

    public Event deleteUserInvite(String creatorId, Event event, String userId) {
        if (!event.isUserCreator(creatorId))
            return null;
        User user = userService.getUserById(userId);
        return (event.deleteUserInvite(user)) ?
                repository.save(event) : null;
    }

    //TODO fixa denna skiten
    public boolean createRsvpRecord(Event event, Group group, Topic topic, String userId) {
        User user = userService.getUserById(userId);
        boolean isUserPartOfInvitedTopic = topic.isUserSubscribed(user);
        boolean check = event.createEventRSVP(group, topic, user, isUserPartOfInvitedTopic);

        if (check) {
            repository.save(event);
            return true;
        } else {
            return false;
        }

    }
}
