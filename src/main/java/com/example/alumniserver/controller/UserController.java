package com.example.alumniserver.controller;


import com.example.alumniserver.httpstatus.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    private final HttpStatusCode statusCode = new HttpStatusCode();

    @GetMapping
    public ResponseEntity<String> getPosts() {
        return new ResponseEntity<>("Test", statusCode.getFoundStatus("Test"));
    }

}
