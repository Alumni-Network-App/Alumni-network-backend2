package com.example.alumniserver.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reply")
@Data
public class Reply {

    @Id
    @Column(name="reply_id")
    private long id;

    @Column(length = 1000)
    private String content;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @ManyToOne
    @JoinColumn(name = "reply_parent_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User user;
}