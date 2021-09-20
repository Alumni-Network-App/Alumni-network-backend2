package com.example.alumniserver.controller;

import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.idhelper.IdHelper;
import com.example.alumniserver.model.Reply;
import com.example.alumniserver.model.User;
import com.example.alumniserver.service.PostService;
import com.example.alumniserver.service.ReplyService;
import com.example.alumniserver.service.UserService;
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
    private final UserService userService;
    private final HttpStatusCode statusCode = new HttpStatusCode();
    private IdHelper idHelper = new IdHelper();

    @Autowired
    public ReplyController(ReplyService service, PostService postService, UserService userService) {
        this.service = service;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping(value = "/{replyId}")
    public ResponseEntity<Reply> getReplyWithId(@PathVariable long replyId,
                                                @RequestHeader("Authorization") String auth) {
        String userId = idHelper.getLoggedInUserId(auth);
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
            @RequestHeader("Authorization") String auth,
            Pageable page) {
        String userId = idHelper.getLoggedInUserId(auth);
        List<Reply> replies = service.getRepliesWithUserId(userId, search, page);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }

    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<List<Reply>> getRepliesToPost(@PathVariable long postId) {
        if (postService.postExists(postId)) {
            List<Reply> replies = service.getRepliesToPostId(postId);
            return new ResponseEntity<>(replies, statusCode.getForbiddenStatus(replies != null));
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/post/{postId}")
    public ResponseEntity<Link> createReply(
            @PathVariable long postId,
            @RequestBody Reply reply,
            @RequestHeader("Authorization") String auth) {
        String userId = idHelper.getLoggedInUserId(auth);
        if (postService.postExists(postId)) {
            Reply addedReply = service.createReply(reply, postId, userId);
            return new ResponseEntity<>(
                    getReplyLinkById(addedReply.getId(), auth),
                    statusCode.getForbiddenPostingStatus(addedReply));
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //Put
    @PutMapping(value = "/{replyId}")
    public ResponseEntity<Link> updateReply(
            @PathVariable long replyId,
            @RequestBody Reply reply,
            @RequestHeader("Authorization") String auth
    ) {
        if (service.replyExists(replyId)) {
            String userId = idHelper.getLoggedInUserId(auth);
            Reply updateReply = service.updateReply(reply, replyId, userId);
            return new ResponseEntity<>(getReplyLinkById(updateReply.getId(), auth),
                    statusCode.getForbiddenStatus(updateReply == null));
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    private Link getReplyLinkById(long replyId, String auth) {
        return linkTo(methodOn(ReplyController.class)
                .getReplyWithId(replyId, auth))
                .withSelfRel();
    }

}
