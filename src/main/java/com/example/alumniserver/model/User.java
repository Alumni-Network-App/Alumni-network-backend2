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
@Table(name="usertable")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

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
    @OneToMany
    @JoinColumn(name = "sender_id")
    private List<Post> posts;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany
    @JoinColumn(name = "sender_id")
    private List<Reply> replies;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="group_member",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")}
    )
    private List<Group> groups;

    public void addGroup(Group group) {
        if(groups == null)
            groups = new ArrayList<>();
        groups.add(group);
    }

    public void addPost(Post post) {
        if(posts == null)
            posts = new ArrayList<>();
        posts.add(post);
    }

    public void addReply(Reply reply) {
        if(replies == null)
            replies = new ArrayList<>();
        replies.add(reply);
    }
/*
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "userRsvp")
    private List<Event> eventRsvp;
 */

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name="event_invite",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private List<Event> events;

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
                        return "/api/v1/group/" + group.getId();
                    }).collect(Collectors.toList());
        } else {
            return null;
        }
    }

}
