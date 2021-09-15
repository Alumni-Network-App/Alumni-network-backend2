package com.example.alumniserver.controller;


import com.example.alumniserver.httpstatus.HttpStatusCode;
import com.example.alumniserver.model.User;
import com.example.alumniserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService service;
    private HttpStatusCode httpStatusCode = new HttpStatusCode();

    @Autowired
    public UserController(UserService service) {
        this.service = service;

    }

    @GetMapping
    public ResponseEntity<Link> getUserLink() {
        if(System.getProperty("alumni.my.uid") == null) {
           // System.setProperty("alumni.my.uid", SecurityContextHolder.getContext().getAuthentication().getName());
        }
        String uid = System.getProperty("alumni.my.uid");
        Link link = getUserLinkById(uid);
        return new ResponseEntity<>(link, HttpStatus.SEE_OTHER);
    }



    @PatchMapping(value = "/update/{userId}")
    public ResponseEntity<Link> UpdateUser(
            @PathVariable String userId,
            @RequestBody User user) {
        if (!service.userExists(userId)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        User updateUser = service.updateUser(userId, user);
        if (updateUser != null)
            return new ResponseEntity<>(
                    getUserLinkById(updateUser.getId()),
                    HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        User user = service.getUserById(userId);
        return new ResponseEntity<>(
                user, httpStatusCode.getBadRequestStatus(user)
        );
    }


    @PostMapping
    public ResponseEntity<Link> addUser(@RequestBody User user) {
        if(System.getProperty("alumni.my.uid") == null) {
           // System.setProperty("alumni.my.uid", SecurityContextHolder.getContext().getAuthentication().getName());
        }
        User addedUser = service.addUser(user);
        return new ResponseEntity<>(getUserLinkById(
                addedUser.getId()),
                HttpStatus.CREATED);
    }

    private Link getUserLinkById(String userId) {
        return linkTo(methodOn(UserController.class)
                .getUserById(userId))
                .withSelfRel();
    }

}
