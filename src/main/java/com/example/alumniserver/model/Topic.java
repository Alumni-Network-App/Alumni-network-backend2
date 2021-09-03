package com.example.alumniserver.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Data
public class Topic {

    @Id
    @Column(name = "topic_id")
    private long id;

    @Column
    private String name;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany
    @JoinColumn(name = "topic_id")
    private List<Post> posts;

    @Column(length = 1000)
    private String description;

}
