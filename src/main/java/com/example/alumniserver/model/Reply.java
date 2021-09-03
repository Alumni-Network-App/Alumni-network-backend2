package com.example.alumniserver.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reply")
@Data
public class Reply {

    @Id
    private long id;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(length = 1000)
    private String content;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "post_id"))
    private Post post;
}