package com.example.alumniserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class RsvpKey implements Serializable {

    @Column(name="user_id")
    private String userId;

    @Column(name="event_id")
    private long eventId;

}
