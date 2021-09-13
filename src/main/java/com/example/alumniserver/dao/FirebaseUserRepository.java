package com.example.alumniserver.dao;

import com.example.alumniserver.model.FirebaseUser;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FirebaseUserRepository {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseUserRepository.class);

    public FirebaseUser save(FirebaseUser user) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> apiFuture = db.collection("users").document(user.getUid()).set(user, SetOptions.merge());
        WriteResult writeResult = apiFuture.get();
        logger.info("Successfully saved, updated time: {}", writeResult.getUpdateTime());
        return user;
    }

    public FirebaseUser get(String uid) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> apiFuture = db.collection("users").document(uid).get();

        DocumentSnapshot document = apiFuture.get();
        if (document.exists()) {
            logger.info("User found: {}", uid);
            return document.toObject(FirebaseUser.class);
        }

        logger.info("User not found: {}", uid);
        return null;
    }
}
