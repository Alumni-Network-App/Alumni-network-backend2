package com.example.alumniserver.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
/*
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name="RSVP",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> userRsvp;
*/
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "events")
    private List<User> invitedUsers;

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

    @JsonGetter("users")
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

    public boolean isUserInvited(String userId) {
        for (User user : invitedUsers) {
            if(user.getId().equals(userId))
                return true;
        }
        return false;
    }

}
