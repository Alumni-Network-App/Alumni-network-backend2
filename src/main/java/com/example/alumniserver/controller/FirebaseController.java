package com.example.alumniserver.controller;


import com.example.alumniserver.dao.UserRepository;
import com.example.alumniserver.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/*@RestController
@RequestMapping("api/v1/firebase/user")
public class FirebaseController {

    @Autowired
    UserRepository userRepository;


    @GetMapping
    public ResponseEntity<User> get() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String uid = authentication.getName();
        User user = userRepository.getById(uid);  //.get(uid);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


//    @PostMapping
//    public ResponseEntity<User> save(@RequestBody String bio) throws  Exception {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        String uid = authentication.getName();
//        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
//
//        User firebaseUser = new User();
//
//        firebaseUser.setName(userRecord.getDisplayName());
//        firebaseUser.setUid(uid);
//
//        FirebaseUser savedFirebaseUser = firebaseUserRepository.save(firebaseUser);
//        return new ResponseEntity<>(savedFirebaseUser, HttpStatus.OK);
//    }
}
*/