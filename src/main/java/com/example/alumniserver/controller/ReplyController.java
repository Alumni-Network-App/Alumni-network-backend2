package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.Reply;
import com.example.alumniserver.service.PostService;
import com.example.alumniserver.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
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
    private final PostService postService;
    private final HttpStatusCode statusCode = new HttpStatusCode();

    @Autowired
    public ReplyController(ReplyService service, PostService postService) {
        this.service = service;
        this.postService = postService;
    }

    @GetMapping(value = "/{replyId}")
    public ResponseEntity<Reply> getReplyWithId(@PathVariable long replyId) {
        String userId = "2";
        Reply reply = service.getReplyWithId(replyId);
        return (reply.getUser().getId().equals(userId)
                ? new ResponseEntity<>(reply,
                statusCode.getBadRequestStatus(reply))
                : new ResponseEntity<>(null, HttpStatus.FORBIDDEN)
        );
    }

    @GetMapping(value = "/user")
    public ResponseEntity<List<Reply>> getRepliesWithUserId(
            @RequestParam(required = false, defaultValue = "") String search,
            Pageable page) {
        String userId = "2";
        List<Reply> replies = service.getRepliesWithUserId(userId, search, page);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }

    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<List<Reply>> getRepliesToPost(@PathVariable long postId) {
        if (postService.postExists(postId)) {
            List<Reply> replies = service.getRepliesToPostId(postId);
            return new ResponseEntity<>(replies, statusCode.getForbiddenStatus(replies == null));
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/post/{postId}")
    public ResponseEntity<Link> createReply(
            @PathVariable long postId,
            @RequestBody Reply reply) {
        String userId = "2";
        if (postService.postExists(postId)) {
            Reply addedReply = service.createReply(reply, postId, userId);
            return new ResponseEntity<>(
                    getReplyLinkById(addedReply.getId()),
                    statusCode.getForbiddenPostingStatus(addedReply));
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //Put
    @PutMapping(value = "/{replyId}")
    public ResponseEntity<Link> updateReply(
            @PathVariable long replyId,
            @RequestBody Reply reply
    ) {
        if (service.replyExists(replyId)) {
            String userId = "2";
            Reply updateReply = service.updateReply(reply, replyId, userId);
            return new ResponseEntity<>(getReplyLinkById(updateReply.getId()),
                    statusCode.getForbiddenStatus(updateReply == null));
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    private Link getReplyLinkById(long replyId) {
        return linkTo(methodOn(ReplyController.class)
                .getReplyWithId(replyId))
                .withSelfRel();
    }
}
