
package com.example.alumniserver.dao;

import com.example.alumniserver.model.Rsvp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RsvpRepository extends JpaRepository<Rsvp, Long> {

}