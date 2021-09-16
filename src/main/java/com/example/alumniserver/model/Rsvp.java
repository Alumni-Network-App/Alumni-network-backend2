package com.example.alumniserver.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;


@Entity
@Table(name = "rsvp")
@Getter
@Setter
public class Rsvp {

    @EmbeddedId
    private RsvpKey id;

    @Column(name = "guest_count")
    private int guestCount;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private Event event;
}