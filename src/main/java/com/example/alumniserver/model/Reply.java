package com.example.alumniserver.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reply")
@Getter
@Setter
public class Reply {

    @Id
    @Column(name="reply_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 1000)
    private String content;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "reply_parent_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User user;

}