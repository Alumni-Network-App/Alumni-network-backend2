package com.example.alumniserver.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class RsvpId implements Serializable {
    @Column(name = "user_id")
    private String userId;

    @Column(name = "event_id")
    private long eventId;

    public RsvpId() {}

    public RsvpId(String userId, long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }
}
