package com.example.alumniserver.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table
@Getter
@Setter
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "topic_id")
    private long id;

    @Column
    private String name;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany
    @JoinColumn(name = "topic_id")
    private List<Post> posts;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "topics")
    private Set<Event> events;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "topicSubscriptions", cascade = CascadeType.ALL)
    private List<User> users;

    @Column(length = 1000)
    private String description;

    public boolean isUserSubscribed(String userId) {
        if(users != null) {
            for (User subbedUser : users) {
                if (subbedUser.getId().equals(userId))
                    return true;
            }
        }
        return false;
    }

    public boolean addEventToTopic(Event event) {
        if(events == null)
            events = new HashSet<>();
        return events.add(event);
    }

    public boolean deleteEventFromTopic(Event event) {
        if(events == null)
            return true;
        return events.remove(event);
    }

    public boolean addUserToTopic(User user) {
        if(users == null)
            users = new ArrayList<>();
        return users.add(user);
    }

    @JsonGetter("posts")
    public List<String> posts() {
        if(posts != null) {
            return posts.stream()
                    .map(post -> {
                        return "/api/v1/post/" + post.getId();
                    }).collect(Collectors.toList());
        } else {
            return null;
        }
    }

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

}
