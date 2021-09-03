package com.example.alumniserver.controller;


import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Topic;
import com.example.alumniserver.model.User;
import com.example.alumniserver.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/topic")
public class TopicController {

    private final HttpStatusCode httpStatusCode = new HttpStatusCode();
    private final TopicService topicService;

    @Autowired
    public TopicController (TopicService topicService){
        this.topicService=topicService;
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResponseEntity<List<Topic>> getAllTopics(){
        List<Topic> topics = topicService.getAllTopics();
        return new ResponseEntity<>(topics, httpStatusCode.getFoundStatus(topics));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Topic> getTopicByID (@PathVariable long id){
        Topic topic = topicService.getTopic(id);
        return new ResponseEntity<>(topic, httpStatusCode.getFoundStatus(topic));
    }

    //TOdO
    //This endpoint should simultaneously create a topic membership record, adding the
    //topic’s creator as the first subscriber to that topic.
    //Detta när vi fixat subscribe
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Topic> createTopic (@RequestBody Topic topic){
        return new ResponseEntity<>(topicService.createTopic(topic), httpStatusCode.getContentStatus());
    }

    //TODO
    //Fixa metod för nedan krav
    //POST /topic/:topic_id/join
    //Create a new topic membership record. Accepts no parameters. user_id is taken as
    //being that of the requesting user and topic_id is provided in the path.



}
