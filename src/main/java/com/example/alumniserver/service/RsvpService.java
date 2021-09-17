package com.example.alumniserver.service;

import com.example.alumniserver.dao.RsvpRepository;
import com.example.alumniserver.model.Rsvp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RsvpService {

    private final RsvpRepository rsvpRepository;


    @Autowired
    public RsvpService(RsvpRepository rsvpRepository) {

        this.rsvpRepository = rsvpRepository;

    }


    public Rsvp saveRsvp(Rsvp rsvp){

        return rsvpRepository.save(rsvp);
    }

}