package com.example.alumniserver.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "\"rsvp\"")
@Getter
@Setter
public class Rsvp {

    @Id
    @Column(name = "user_id")
    private String id;

    @PreUpdate
    protected void onUpdate(){
        last_updated = new Date();
    }

    @Column(name = "guest_count")
    private int guest_count;



    @Column(name = "last_updated")
    private Date last_updated;

    @Id
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @Id
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "eventId", referencedColumnName = "eventId")
    private Event event;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<User> users;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Event> events;

    public void addUserToRsvp(User user){
        if(users == null){
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public void addEventToRsvp(Event event){
        if(events == null){
            events = new ArrayList<>();
        }
        events.add(event);
    }
}
