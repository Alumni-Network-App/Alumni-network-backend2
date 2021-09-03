package com.example.alumniserver.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "\"group\"")
@Data
public class Group {

    @Id
    @Column(name = "group_id")
    private long id;

    @Column
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "is_private")
    private boolean isPrivate;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name="event_group",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private List<Event> events;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "groups")
    private List<User> users;

}
