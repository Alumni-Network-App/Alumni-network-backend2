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
        return repository.findEventByID(eventId);
    }

    public boolean updateAnEvent(Event event, long postId, String userId) {
        Event fetchedEvent = repository.getById(postId);
        event.setId(postId);
        if(fetchedEvent.getId() == event.getId() && checkIfUserAllowedToUpdate(event, userId)) {
            repository.save(updateFields(event, fetchedEvent));
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfUserAllowedToUpdate(Event event, String userId){
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

        if(!updatedEvent.getStartTime().equals("") && !updatedEvent.getEndTime().equals(""))
            oldEvent.setStartTime(updatedEvent.getStartTime());
            oldEvent.setEndTime(updatedEvent.getEndTime());

        if (!updatedEvent.getBannerImg().equals(""))
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
