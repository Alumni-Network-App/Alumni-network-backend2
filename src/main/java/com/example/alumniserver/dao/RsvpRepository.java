package com.example.alumniserver.dao;

import com.example.alumniserver.model.Rsvp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RsvpRepository extends JpaRepository<Rsvp, Long> {
    @Query("SELECT r FROM Rsvp r WHERE r.event.id = : eventId AND r.user.id = : userId")
    Page<Rsvp> getRsvps(@Param("eventId") long eventId,
                        @Param("userId") String userId,
                        Pageable page);
    Rsvp getRsvpById(String rsvpId);






}
