package com.example.alumniserver.dao;

import com.example.alumniserver.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event findEventById(long id);

    @Query("SELECT DISTINCT e FROM Event e " +
            "INNER JOIN e.topic.users u " +
            "INNER JOIN e.invitedUsers eiu " +
            "INNER JOIN e.groups g " +
            "INNER JOIN g.users gu " +
            "WHERE e.user.id = :userId OR u.id = :userId OR eiu.id = :userId OR gu.id = :userId " +
            "ORDER BY e.id")
    List<Event> selectUserSubscribedEvents(@Param("userId") String userId);


}
