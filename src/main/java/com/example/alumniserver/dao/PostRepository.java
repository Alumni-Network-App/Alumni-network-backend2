package com.example.alumniserver.dao;

import com.example.alumniserver.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserId(String id);

    List<Post> findAllByReceiverTypeAndReceiverId(String type, String id);
/*
    List<Post> findAllByReceiverTypeAndReceiverIdAndUserId(String type, long receiverId, long senderId);
*/
    List<Post> findAllByReceiverTypeAndReceiverIdAndUserId(String type, String receiverId, String senderId);

    List<Post> findAllByUserIdAndTopicId(String userId, long topicId);

    Post findPostById(long postId);

}
