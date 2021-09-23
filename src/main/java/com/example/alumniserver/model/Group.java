package com.example.alumniserver.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "\"group\"")
@Getter
@Setter
public class Group {

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long groupId;

    @Column
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "is_private")
    private boolean isPrivate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public void setLastUpdated() {
        lastUpdated = LocalDateTime.now(Clock.systemDefaultZone());
    }

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "groups", cascade = CascadeType.ALL)
    private Set<Event> events;


    @ManyToMany
    @JoinTable(
            name="group_member",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> users;

    @JsonGetter("users")
    public List<String> users() {
        if(users != null) {
            return users.stream()
                    .map(user -> {
                        return "/api/v1/user/" + user.getId();
                    }).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @JsonGetter("events")
    public List<String> events() {
        if(events != null) {
            return events.stream()
                    .map(event -> {
                        return "/api/v1/event/" + event.getId();
                    }).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public boolean isUserMember(String id) {
        for (User user : users) {
            if(user.getId().equals(id))
                return true;
        }
        return false;
    }

    public void addUserAsMember(User user) {
        if(users == null){
            users = new HashSet<>();
        }
        users.add(user);
    }

    public boolean removeEventFromGroup(Event event) {
        return events.remove(event);
    }
}
