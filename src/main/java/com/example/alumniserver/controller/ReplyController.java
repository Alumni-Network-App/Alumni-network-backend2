package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Reply;
import com.example.alumniserver.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<Reply>> getRepliesWithUserId(@PathVariable long userId) {
        List<Reply> replies = service.getRepliesWithUserId(userId);
        return new ResponseEntity<>(replies, statusCode.getFoundStatus(replies));
    }

    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<List<Reply>> getRepliesToPost(@PathVariable long postId) {
        List<Reply> replies = service.getRepliesToPostId(postId);
        return new ResponseEntity<>(replies, statusCode.getFoundStatus(replies));
    }

    @PostMapping(value = "/post/{postId}")
    public ResponseEntity<Boolean> createReply(
            @PathVariable long postId,
            @RequestBody Reply reply) {
        long userId = 2;
        boolean added = service.createReply(reply, postId, userId);
        return new ResponseEntity<>(added, statusCode.getForbiddenStatus(added));
    }

    //Put
    @PutMapping(value = "/{replyId}")
    public ResponseEntity<Boolean> updateReply(
            @PathVariable long replyId,
            @RequestBody Reply reply
    ) {
        boolean added = service.updateReply(reply, replyId);
        return new ResponseEntity<>(added, statusCode.getForbiddenStatus(added));
    }
}
