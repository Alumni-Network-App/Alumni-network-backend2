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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
    private ZonedDateTime date = LocalDateTime.now().atZone(ZoneId.of("CET"));

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

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name="event_group",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")}
    )
    private Set<Group> groups;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name="event_topic_invite",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "topic_id")}
    )
    private Set<Topic> topics;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name="event_invite",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> invitedUsers;

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

    @JsonGetter("topics")
    public List<String> topics() {
        if(topics != null) {
            return topics.stream()
                    .map(topic -> {
                        return "/api/v1/topic/" + topic.getId();
                    }).collect(Collectors.toList());
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
        if(groups != null) {
            for (Group group : groups) {
                if (group.getId() == groupId)
                    return true;
            }
        }
        return false;
    }

    public boolean isTopicInvited(long topicId) {
        if(topics != null) {
            for (Topic topic : topics) {
                if (topic.getId() == topicId)
                    return true;
            }
        }
        return false;
    }

    public boolean isUserSubscribedToAnyTopic(String userId) {
        if(topics != null) {
            for(Topic topic : topics)
                if(topic.isUserSubscribed(userId))
                    return true;
        }
        return false;
    }

    public boolean isUserCreator(String userId){
        return user.getId().equals(userId);
    }

    public boolean inviteGroup(Group group, String userId){
        if(groups == null) {
            groups = new HashSet<>();
            return groups.add(group);
        }
        return ((!group.isPrivate()
                || group.isUserMember(userId))
                && groups.add(group));
    }

    public boolean inviteTopic(Topic topic, String userId){
        if(topics == null)
            topics = new HashSet<>();
        if (!topic.isUserSubscribed(userId)) return false;
        return topics.add(topic);

    }

    public boolean deleteGroupInvite(Group group){
        return groups != null && groups.remove(group);
    }

    public Group getGroupInvite(int groupNumber) {
        return groups.stream().toList().get(groupNumber);
    }

    public boolean deleteTopicInvite(Topic topic){
        return topics != null && topics.remove(topic);
    }

    public boolean setUserInvite(User user){
        if (invitedUsers == null)
            invitedUsers = new HashSet<>();
        if (isUserInvited(user.getId())) return false;
        return invitedUsers.add(user);
    }

    public boolean deleteUserInvite(User user){
        return invitedUsers != null
                && invitedUsers.remove(user);
    }

    @JsonIgnore
    public int getNumberOfGroupInvites() {
        return (groups == null) ? 0 : groups.size();
    }

    @JsonIgnore
    public int getNumberOfTopicInvites() {
        return (topics == null) ? 0 : topics.size();
    }

    @JsonIgnore
    public int getTotalInvitedUsers() {
        return (invitedUsers == null) ? 0 : invitedUsers.size();
    }

    public boolean isUserPartOfInvitedGroups(String userId) {
        if(groups != null){
            for(Group group : groups)
                if(group.isUserMember(userId))
                    return true;
        }
        return false;

    }
}
