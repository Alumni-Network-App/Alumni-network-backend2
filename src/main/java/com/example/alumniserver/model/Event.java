package com.example.alumniserver.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "\"event\"")
@Data
public class Event {

    @Id
    @Column(name = "event_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User user;

    @Column
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "banner_img")
    private String bannerImg;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "events")
    private List<Group> groups;


}
