package com.example.alumniserver.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="usertable")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "user_id")
    private String id;

    @Column
    private String name;

    @Column
    private String picture;

    @Column(length = 1000)
    private String status;

    @Column(length = 1000)
    private String bio;

    @Column(name = "fun_fact", length = 1000)
    private String funFact;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id")
    private List<Post> posts;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id")
    private List<Reply> replies;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "users")
    private Set<Group> groups;

    public boolean addTopicToSubscription(Topic topic) {
        if(topicSubscriptions == null)
            topicSubscriptions = new ArrayList<>();
        return topicSubscriptions.add(topic);
    }

    @OneToMany(mappedBy = "user")
    private List<Rsvp> rsvps;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "invitedUsers")
    private Set<Event> events;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name="topic_subscription",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "topic_id")}
    )
    private List<Topic> topicSubscriptions;

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

    @JsonGetter("topicSubscriptions")
    public List<String> topicSubscriptions() {
        if(topicSubscriptions != null) {
            return topicSubscriptions.stream()
                    .map(topic -> {
                        return "/api/v1/topic/" + topic.getId();
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

    @JsonGetter("groups")
    public List<String> groups() {
        if(groups != null) {
            return groups.stream()
                    .map(group -> {
                        return "/api/v1/group/" + group.getGroupId();
                    }).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public boolean addEventToUser(Event event) {
        if(events == null)
            events = new HashSet<>();
        return events.add(event);
    }

    public boolean deleteEventToUser(Event event) {
        if(events == null)
            return true;
        return events.remove(event);
    }

}
