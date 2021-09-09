package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Reply;
import com.example.alumniserver.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping("/api/v1/reply")
public class ReplyController {

    private final ReplyService service;
    private final HttpStatusCode statusCode = new HttpStatusCode();

    @Autowired
    public ReplyController(ReplyService service) {
        this.service = service;
    }

    @GetMapping(value = "/{replyId}")
    public ResponseEntity<Reply> getReplyWithId(@PathVariable long replyId) {
        Reply reply = service.getReplyWithId(replyId);
        return new ResponseEntity<>(reply, statusCode.getFoundStatus(reply));
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<Reply>> getRepliesWithUserId(@PathVariable String userId) {
        List<Reply> replies = service.getRepliesWithUserId(userId);
        return new ResponseEntity<>(replies, statusCode.getFoundStatus(replies));
    }

    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<List<Reply>> getRepliesToPost(@PathVariable long postId) {
        List<Reply> replies = service.getRepliesToPostId(postId);
        return new ResponseEntity<>(replies, statusCode.getFoundStatus(replies));
    }

    @PostMapping(value = "/post/{postId}")
    public ResponseEntity<Link> createReply(
            @PathVariable long postId,
            @RequestBody Reply reply) {
        String userId = "2";
        Reply addedReply = service.createReply(reply, postId, userId);
        return new ResponseEntity<>(getReplyLinkById(addedReply.getId()),
                statusCode.getForbiddenStatus(addedReply == null));
    }

    //Put
    @PutMapping(value = "/{replyId}")
    public ResponseEntity<Link> updateReply(
            @PathVariable long replyId,
            @RequestBody Reply reply
    ) {
        Reply updateReply = service.updateReply(reply, replyId);
        return new ResponseEntity<>(getReplyLinkById(updateReply.getId()),
                statusCode.getForbiddenStatus(updateReply == null));
    }

    private Link getReplyLinkById(long replyId) {
        return linkTo(methodOn(ReplyController.class)
                .getReplyWithId(replyId))
                .withSelfRel();
    }
}
