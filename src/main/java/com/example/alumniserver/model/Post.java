package com.example.alumniserver.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="post")
@Data
public class Post<T> {

    @Id
    @Column(name = "post_id")
    private long id;

    @Column(name = "last_updated")
    private LocalDateTime date;

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
    @OneToMany
    @JoinColumn(name="reply_parent_id")
    private List<Reply> replies;

    @Column(name = "receiver_type")
    private String receiverType;

    @Column(name="receiver_id")
    private long receiverId;
}
