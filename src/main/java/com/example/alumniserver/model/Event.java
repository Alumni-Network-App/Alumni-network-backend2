package com.example.alumniserver.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "\"event\"")
@Getter
@Setter
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne(cascade = CascadeType.ALL)
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

    @ManyToMany(mappedBy = "events")
    private List<Group> groups;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="RSVP",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> userRsvp;

    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "events", cascade = CascadeType.ALL)
    private List<User> invitedUsers;

    @OneToMany(mappedBy = "event")
    private List<Rsvp> rsvps;

    @JsonGetter("groups")
    public List<String> groups() {
        if(groups != null) {
            return groups.stream()
                    .map(group -> {
                        return "/api/v1/group/" + group.getId();
                    }).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @JsonGetter("invitedUsers")
    public List<String> users() {
        if(invitedUsers != null) {
            return invitedUsers.stream()
                    .map(user -> {
                        return "/api/v1/user/" + user.getId();
                    }).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @JsonGetter("creator")
    public String user() {
        if(user != null) {
            return "/api/v1/user/" + user.getId();
        } else {
            return null;
        }
    }

    @JsonGetter("topic")
    public String topic() {
        if(topic != null) {
            return "/api/v1/topic/" + topic.getId();
        } else {
            return null;
        }
    }

    public boolean isUserInvited(String userId) {
        for (User user : invitedUsers) {
            if(user.getId().equals(userId))
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

    public boolean isTopicInvited(long topicId) {
        return topic != null && topic.getId() == topicId;
    }

    public boolean isUserCreator(String userId){
        return user.getId().equals(userId);
    }

    public boolean inviteGroup(Group group, String userId){
        if(!isGroupInvited(group.getId()) && (!group.isPrivate() || group.isUserMember(userId))){
            groups.add(group);
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteGroupInvite(Group group){
        return groups.remove(group);
    }

    public Group getGroupInvite(int groupNumber) {
        return groups.get(groupNumber);
    }

    public void setGroupInvite(Group group, int groupNumber) {
        if(groups != null)
            groups.set(groupNumber, group);
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

    @JsonIgnore
    public int getNumberOfGroupInvites() {
        return (groups == null) ? 0 : groups.size();
    }

    public boolean isUserPartOfInvitedGroups(User user) {
        if(groups != null){
            for(Group group : groups)
                if(group.isUserMember(user.getId()))
                    return true;
        }
        return false;

    }
}
