package com.example.alumniserver.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="usertable")
@Data
public class User {

    @Id
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
    @JoinColumn(name = "event_id")
    private List<Event> events;

}
