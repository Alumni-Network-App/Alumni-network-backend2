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
            "LEFT JOIN e.topics t " +
            "LEFT JOIN t.users tu " +
            "INNER JOIN e.invitedUsers eiu " +
            "LEFT JOIN e.groups g " +
            "LEFT JOIN g.users gu " +
            "WHERE (e.user.id = :userId) OR " +
            "(e.topics.size > 0 AND tu.id = :userId) OR " +
            "(eiu.id = :userId) OR " +
            "(e.groups.size > 0 AND gu.id = :userId) " +
            "ORDER BY e.id")
    List<Event> selectUserSubscribedEvents(@Param("userId") String userId);

}
