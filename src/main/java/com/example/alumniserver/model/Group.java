package com.example.alumniserver.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "\"group\"")
@Getter
@Setter
public class Group {

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "is_private")
    private boolean isPrivate;


    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="event_group",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private List<Event> events;


    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "groups", cascade = CascadeType.ALL)
    private List<User> users;

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
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public boolean removeEventFromGroup(Event event) {
        return events.remove(event);
    }

    public boolean addEventToGroup(Event event) {
        return events.add(event);
    }
}
