package com.example.alumniserver.service;

import com.example.alumniserver.dao.PostRepository;
import com.example.alumniserver.dao.ReplyRepository;
import com.example.alumniserver.dao.RsvpRepository;
import com.example.alumniserver.dao.UserRepository;
import com.example.alumniserver.model.Reply;
import com.example.alumniserver.model.Rsvp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RsvpService {

    private final RsvpRepository rsvpRepository;


    @Autowired
    public RsvpService(RsvpRepository rsvpRepository) {

        this.rsvpRepository = rsvpRepository;

    }


    public List<Rsvp> getRsvps(Long eventId, String userId, Pageable page) {
        return rsvpRepository.getRsvps(eventId, userId, page).getContent();
    }

    public Rsvp getRsvpById(String rsvpId){
        return rsvpRepository.getRsvpById(rsvpId);
    }


    public Rsvp saveRsvp(Rsvp rsvp){

        return rsvpRepository.save(rsvp);
    }

}