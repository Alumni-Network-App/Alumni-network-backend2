package com.example.alumniserver.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table
@Getter
@Setter
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToMany
    @JoinColumn(name = "topic_id")
    private List<Event> events;

    @Column(length = 1000)
    private String description;

    @JsonGetter("posts")
    public List<String> posts() {
        if(posts != null) {
            return posts.stream()
                    .map(post -> {
                        return "/api/v1/posts/" + post.getId();
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
                        return "/api/v1/events/" + event.getId();
                    }).collect(Collectors.toList());
        } else {
            return null;
        }
    }

}
