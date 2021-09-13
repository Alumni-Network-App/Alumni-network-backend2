package com.example.alumniserver.controller;

import com.example.alumniserver.dao.FirebaseUserRepository;
import com.example.alumniserver.model.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/firebase/user")
public class FirebaseController {

    @Autowired
    FirebaseUserRepository firebaseUserRepository;

    @GetMapping
    public ResponseEntity<FirebaseUser> get() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String uid = authentication.getName();
        FirebaseUser user = firebaseUserRepository.get(uid);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<FirebaseUser> save(@RequestBody String bio) throws  Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String uid = authentication.getName();
        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);

        FirebaseUser firebaseUser = new FirebaseUser();

        firebaseUser.setName(userRecord.getDisplayName());
        firebaseUser.setUid(uid);

        FirebaseUser savedFirebaseUser = firebaseUserRepository.save(firebaseUser);
        return new ResponseEntity<>(savedFirebaseUser, HttpStatus.OK);
    }
}
