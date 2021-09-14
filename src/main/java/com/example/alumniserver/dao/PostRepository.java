package com.example.alumniserver.dao;

import com.example.alumniserver.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND CONCAT(p.content, p.title) LIKE %:search%")
    Page<Post> getPosts(@Param("userId") String userId,
                        @Param("search") String search,
                        Pageable page);

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND p.receiverType = :receiverType AND CONCAT(p.content, p.title) LIKE %:search%")
    Page<Post> getFilteredPosts(
            @Param("userId") String userId,
            @Param("receiverType") String receiverType,
            @Param("search") String search,
            Pageable page);

    /*
        Page<Post> findAllByUserId(String id, Pageable page);

        Page<Post> findAllByUserIdAndReceiverType(String userId, String receiverType, Pageable page);
    */
    @Query("SELECT p FROM Post p WHERE p.id = :id AND p.receiverType = :type AND CONCAT(p.content, p.title) LIKE %:filter%")
    Page<Post> getFilteredPostsToTypeWithId(@Param("type") String type,
                                            @Param("id") String id,
                                            @Param("filter") String filter,
                                            Pageable page);

    @Query("SELECT p FROM Post p WHERE p.user.id = :senderId AND p.receiverType = :type AND p.receiverId = :receiverId AND CONCAT(p.content, p.title) LIKE %:filter%")
    Page<Post> getFilteredPostsToTypeWithIdFromUser(@Param("type") String type,
                                                             @Param("receiverId") String receiverId,
                                                             @Param("senderId") String senderId,
                                                             @Param("filter") String filter,
                                                             Pageable page);

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND p.topic.id = :topicId AND CONCAT(p.content, p.title) LIKE %:filter%")
    Page<Post> getFilteredPostsToTopic(@Param("userId") String userId,
                                         @Param("topicId") long topicId,
                                         @Param("filter") String filter,
                                         Pageable page);

    Post findPostById(long postId);

}
