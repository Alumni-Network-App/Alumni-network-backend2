package com.example.alumniserver.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseService {

    @PostConstruct
    public void initializeFirebaseApp() throws IOException {
        InputStream serviceAccount = this.getClass().getResourceAsStream("/firebase_config.json");

        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(firebaseOptions);
    }

}
