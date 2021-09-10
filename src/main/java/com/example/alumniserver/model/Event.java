package com.example.alumniserver.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "\"event\"")
@Getter
@Setter
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User user;

    @Column
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "banner_img")
    private String bannerImg;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "events")
    private List<Group> groups;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name="RSVP",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> userRsvp;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "events")
    private List<User> invitedUsers;

    public boolean isUserInvited(long userId) {
        for (User user : invitedUsers) {
            if(user.getId() == userId)
                return true;
        }
        return false;
    }

    public boolean isGroupInvited(long groupId) {
        for (Group group : groups) {
            if(group.getId() == groupId)
                return true;
        }
        return false;
    }

    public boolean isUserCreator(long userId){
        if(user.getId() == userId){
            return true;
        }else{
            return false;
        }
    }

    public boolean inviteGroup(Group group){
        if(!isGroupInvited(group.getId())){
            groups.add(group);
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteGroupInvite(Group group){
        if(isGroupInvited(group.getId())){
            groups.remove(group);
            return true;
        }else{
            return false;
        }
    }

    public boolean setInviteTopic(Topic topic){
        if(this.topic==null){
            this.topic=topic;
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteInviteTopic(Topic topic){
        if(this.topic==topic){
            this.topic=null;
            return true;
        }else{
            return false;
        }
    }

    public boolean setUserInvite(User user){
        if(!isUserInvited(user.getId())){
            invitedUsers.add(user);
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteUserInvite(User user){
        if(isUserInvited(user.getId())){
            invitedUsers.remove(user);
            return true;
        }else{
            return false;
        }
    }

    //TODO för RSVP event delen
    public boolean createEventRSVP(Group group, Topic topic, User user, boolean isUserPartOfInvitedTopic){

        if(isUserInvited(user.getId()) || (isGroupInvited(group.getId()) && group.isUserMember(user.getId())) || isUserPartOfInvitedTopic){
            userRsvp.add(user);
            return true;
        }else{
            return false;
        }
    }
}
