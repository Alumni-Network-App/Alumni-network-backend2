package com.example.alumniserver.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import java.rmi.server.UID;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name="post")
@Getter
@Setter
public class Post<T> {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "last_updated")
    private LocalDateTime date = LocalDateTime.now();

    @Column(length = 1000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Getter
    @Column(length = 1000)
    private String title;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private User user;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="reply_parent_id")
    private List<Reply> replies;

    @Column(name = "receiver_type", updatable = false)
    private String receiverType;

    @Column(name="receiver_id", updatable = false)
    private String receiverId;

    public void addReply(Reply reply) {
        if(replies == null)
            replies = new ArrayList<>();
        replies.add(reply);
    }

    @JsonGetter("topic")
    public String topic() {
        if(topic != null) {
            return "/api/v1/topic/" + topic.getId();
        } else {
            return null;
        }
    }

    @JsonGetter("user")
    public String user() {
        if(user != null) {
            return "/api/v1/user/" + user.getId();
        } else {
            return null;
        }
    }

    @JsonGetter("replies")
    public List<String> replies() {
        if(replies != null) {
            return replies.stream()
                    .map(reply -> {
                        return "/api/v1/reply/" + reply.getId();
                    }).collect(Collectors.toList());
        } else {
            return null;
        }
    }

}
