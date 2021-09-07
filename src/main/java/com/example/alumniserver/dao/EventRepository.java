package com.example.alumniserver.dao;

import com.example.alumniserver.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByUserId(long id);

    Event findEventByID(long id);
}
