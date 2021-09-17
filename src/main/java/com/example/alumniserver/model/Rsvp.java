package com.example.alumniserver.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "rsvp")
@Getter
@Setter
public class Rsvp {

    @EmbeddedId
    private RsvpId rsvpId;

    @Column(name = "guest_count")
    private int guestCount;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private Event event;

    @JsonGetter("user")
    public String user() {
        return(user != null) ? "/api/v1/user/" + user.getId() : null;
    }

    @JsonGetter("event")
    public String event() {
        return(event != null) ? "/api/v1/event/" + event.getId() : null;
    }
}