package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.idhelper.IdHelper;
import com.example.alumniserver.model.Topic;
import com.example.alumniserver.model.User;
import com.example.alumniserver.service.TopicService;
import com.example.alumniserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping("/api/v1/topic")
public class TopicController {

    private final TopicService service;
    private final UserService userService;
    private final HttpStatusCode status = new HttpStatusCode();
    private IdHelper idHelper = new IdHelper();

    @Autowired
    public TopicController(TopicService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Topic>> getTopics(@RequestParam(required = false, defaultValue = "") String name, Pageable page) {
        return new ResponseEntity<>(service.getTopics(page, name), HttpStatus.OK);
    }

    @GetMapping(value = "/{topicId}")
    public ResponseEntity<Topic> getTopic(@PathVariable long topicId) {
        Topic topic = service.getTopic(topicId);
        return new ResponseEntity<>(topic, status.getBadRequestStatus(topic));
    }

    @PostMapping
    public ResponseEntity<Link> postTopic(@RequestBody Topic topic) {
        String userId = idHelper.getLoggedInUserId();
        topic = service.createTopic(topic, userId);
        return new ResponseEntity<>
                (getTopicLinkById(topic), status.getForbiddenPostingStatus(topic));
    }

    @PostMapping(value = "/{topicId}/join")
    public ResponseEntity<Link> postTopicSubscription(@PathVariable long topicId) {
        String userId = idHelper.getLoggedInUserId();
        Topic topic = service.getTopic(topicId);
        User user = userService.getUserById(userId);
        if(topic == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return (topic.isUserSubscribed(userId)) ? new ResponseEntity<>(
                getTopicLinkById(topic), HttpStatus.SEE_OTHER)
                : new ResponseEntity<>(getTopicLinkById(
                        service.createTopicSubscription(topic, user)),
                        HttpStatus.CREATED);

    }

    private Link getTopicLinkById(Topic topic) {
        return (topic == null) ? null :
        linkTo(methodOn(TopicController.class)
                .getTopic(topic.getId()))
                .withSelfRel();
    }


}
