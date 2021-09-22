package com.example.alumniserver.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "reply")
@Getter
@Setter
public class Reply {

    @Id
    @Column(name="reply_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(length = 1000)
    private String content;

    @Column(name = "last_updated")
    private ZonedDateTime date = ZonedDateTime.now(ZoneId.of("CET"));

    @ManyToOne
    @JoinColumn(name = "reply_parent_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User user;

    @JsonGetter("user")
    public String user() {
        if(user != null) {
            return "/api/v1/user/" + user.getId();
        } else {
            return null;
        }
    }

    @JsonGetter("post")
    public String post() {
        if(post != null) {
            return "/api/v1/post/" + post.getId();
        } else {
            return null;
        }
    }

}