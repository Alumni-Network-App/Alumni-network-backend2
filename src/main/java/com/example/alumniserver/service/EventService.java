package com.example.alumniserver.service;

import com.example.alumniserver.dao.EventRepository;
import com.example.alumniserver.dao.GroupRepository;
import com.example.alumniserver.dao.TopicRepository;
import com.example.alumniserver.dao.UserRepository;
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
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public EventService (EventRepository repository, UserRepository userRepository, GroupRepository groupRepository, TopicRepository topicRepository){
        this.repository = repository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.topicRepository = topicRepository;
    }

    public boolean eventExists(long eventId) {
        return repository.existsById(eventId);
    }

    public List<Event> getAllUserEvents(String id) {
        return repository.findAllByUserId(id);
    }

    public Event createEvent(Event event){
        return repository.save(event);
    }

    public Event getEvent(long eventId) {
        return repository.findEventById(eventId);
    }

    public Event updateAnEvent(Event newEvent, Event oldEvent, String userId) {
        return (oldEvent.getUser().getId().equals(userId))
                ? repository.save(updateFields(newEvent, oldEvent)) : null;
    }

    private Event updateFields(Event updatedEvent, Event oldEvent) {
        if(updatedEvent.getName() != null)
            oldEvent.setName(updatedEvent.getName());

        if(updatedEvent.getDescription() != null)
            oldEvent.setDescription(updatedEvent.getDescription());

        if(updatedEvent.getStartTime() != null)
            oldEvent.setStartTime(updatedEvent.getStartTime());

        if(updatedEvent.getEndTime() != null)
            oldEvent.setEndTime(updatedEvent.getEndTime());

        if (updatedEvent.getBannerImg() != null)
            oldEvent.setBannerImg(updatedEvent.getBannerImg());

        return oldEvent;

    }

    public boolean createEventInviteForGroup(Event event, long groupId){
        Group group = groupRepository.getById(groupId);
        boolean check;
        check = event.inviteGroup(group);
        if(check){
            repository.save(event);
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteEventInviteForGroup(Event event, long groupId){
        Group group = groupRepository.getById(groupId);
        boolean check;
        check = event.deleteGroupInvite(group);
        if(check){
            repository.save(event);
            return true;
        }else{
            return false;
        }
    }

    public boolean createEventTopicInvite(Event event, long topicId){
        Topic topic = topicRepository.getById(topicId);
        boolean check;
        check = event.setInviteTopic(topic);
        if(check && topic!=null){
            repository.save(event);
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteEventTopicInvite(Event event, long topicId){
        Topic topic = topicRepository.getById(topicId);
        boolean check;
        check = event.deleteInviteTopic(topic);
        if(check){
            repository.save(event);
            return true;
        }else{
            return false;
        }
    }

    public boolean createUserInvite(Event event, String userId){
        User user = userRepository.getById(userId);
        boolean check = event.setUserInvite(user);
        if(check){
            repository.save(event);
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteUserInvite(Event event, String userId){
        User user = userRepository.getById(userId);
        boolean check = event.deleteUserInvite(user);
        if(check){
            repository.save(event);
            return true;
        }else{
            return false;
        }
    }

    //TODO fixa denna skiten
    public boolean createRsvpRecord(Event event, Group group, Topic topic, String userId){
        User user = userRepository.getById(userId);
        boolean isUserPartOfInvitedTopic = topic.isUserSubscribed(user);
        boolean check = event.createEventRSVP(group, topic, user, isUserPartOfInvitedTopic);

        if(check){
            repository.save(event);
            return true;
        }else{
            return false;
        }

    }
}
