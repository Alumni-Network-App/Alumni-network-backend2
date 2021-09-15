package com.example.alumniserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "\"rsvp\"")
@Getter
@Setter
public class Rsvp {

    @Id
    @Column(name = "user_id")
    private String id;

    @Column(name = "guest_count")
    private int guest_count;

    @Column(name = "last_updated")
    private Date last_updated;

    @Id
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "eventId", referencedColumnName = "eventId")
    private Event event;

}
