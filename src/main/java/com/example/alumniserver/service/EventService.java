package com.example.alumniserver.service;

import com.example.alumniserver.dao.EventRepository;
import com.example.alumniserver.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository repository;
    private final UserService userService;
    private final GroupService groupService;
    private final TopicService topicService;
    private final RsvpService rsvpService;

    @Autowired
    public EventService(EventRepository repository,
                        UserService userService,
                        GroupService groupService,
                        TopicService topicService,
                        RsvpService rsvpService) {
        this.repository = repository;
        this.userService = userService;
        this.groupService = groupService;
        this.topicService = topicService;
        this.rsvpService = rsvpService;
    }

    public boolean eventExists(long eventId) {
        return repository.existsById(eventId);
    }

    public List<Event> getAllUserEvents(String userId) {
        return repository.selectUserSubscribedEvents(userId);
    }

    public Event getEvent(long eventId) {
        return repository.findEventById(eventId);
    }

    public Event createEvent(Event event, String userId) {
        int groupInvites = event.getNumberOfGroupInvites();
        User user = userService.getUserById(userId);
        event.setUser(user);
        event.setUserInvite(user);

        if (groupInvites > 0)
            return (isValidGroupInvites(event, groupInvites, userId)) ?
                    repository.save(event) : null;

        return (event.getNumberOfTopicInvites() > 0) ? repository.save(event) : null;
    }

    private boolean isValidGroupInvites(Event event, int groupInvites, String userId) {
        for (int i = 0; i < groupInvites; i++) {
            Group group = groupService.getGroup(event.getGroupInvite(i).getId());
            if (group == null || !group.isUserMember(userId))
                return false;
        }
        return true;
    }

    public Event updateAnEvent(Event newEvent, Event oldEvent, String userId) {
        return (oldEvent.isUserCreator(userId))
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
        return event.inviteGroup(group, userId)
                ? repository.save(event) : null;
    }

    public Event deleteEventInviteForGroup(String userId, Event event, long groupId) {
        if (!event.isUserCreator(userId))
            return null;
        Group group = groupService.getGroup(groupId);
        return (event.deleteGroupInvite(group)
                && groupService.removeEventFromGroup(event, group) != null)
                ? repository.save(event) : null;
    }

    public Event createEventTopicInvite(String userId, Event event, long topicId) {
        if (!event.isUserCreator(userId))
            return null;
        Topic topic = topicService.getTopic(topicId);
        return (event.inviteTopic(topic, userId)
                && topicService.addEventToTopic(event, topic) != null)
                ? repository.save(event) : null;
    }

    public Event deleteEventTopicInvite(String userId, Event event, long topicId) {
        if (!event.isUserCreator(userId))
            return null;
        Topic topic = topicService.getTopic(topicId);
        return (event.deleteTopicInvite(topic)
                && topicService.deleteEventFromTopic(event, topic) != null)
                ? repository.save(event) : null;
    }

    public Event createUserInvite(String creatorId, Event event, String userId) {
        if (!event.isUserCreator(creatorId))
            return null;
        User user = userService.getUserById(userId);
        return (event.setUserInvite(user)
                && userService.addEventToUser(event, user) != null)
                ? repository.save(event) : null;
    }

    public Event deleteUserInvite(String creatorId, Event event, String userId) {
        if (!event.isUserCreator(creatorId))
            return null;
        User user = userService.getUserById(userId);
        return (event.deleteUserInvite(user)
                && userService.deleteEventFromUser(event, user) != null)
                ? repository.save(event) : null;
    }


    public Rsvp createRsvpRecord(Event event,Rsvp rsvp, String userId) {
        User user = userService.getUserById(userId);
        if (isUserInvitedToEvent(user, event)) {
            rsvp.setRsvpId(new RsvpId(user.getId(), event.getId()));
            rsvp.setEvent(event);
            rsvp.setUser(user);
            if(rsvp.getGuestCount() < 1)
                rsvp.setGuestCount(1);
            return rsvpService.saveRsvp(rsvp);
        } else
            return null;

    }

    private boolean isUserInvitedToEvent(User user, Event event) {
        return (event.isUserSubscribedToAnyTopic(user.getId()))
                || event.isUserPartOfInvitedGroups(user) || event.isUserInvited(user.getId()) || event.isUserCreator(user.getId());
    }
}
