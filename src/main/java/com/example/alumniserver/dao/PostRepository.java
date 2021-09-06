package com.example.alumniserver.dao;

import com.example.alumniserver.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserId(long id);

    List<Post> findAllByReceiverTypeAndReceiverId(String type, long id);

    List<Post> findAllByReceiverTypeAndReceiverIdAndUserId(String type, long receiverId, long senderId);

    List<Post> findAllByUserIdAndTopicId(long userId, long topicId);

}
