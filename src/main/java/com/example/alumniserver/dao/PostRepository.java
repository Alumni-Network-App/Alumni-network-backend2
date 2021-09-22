package com.example.alumniserver.dao;

import com.example.alumniserver.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND CONCAT(p.content, p.title) LIKE %:search% ORDER BY p.lastUpdated DESC")
    Page<Post> getPosts(@Param("userId") String userId,
                        @Param("search") String search,
                        Pageable page);

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND p.receiverType = :receiverType AND CONCAT(p.content, p.title) LIKE %:search% ORDER BY p.lastUpdated DESC")
    Page<Post> getFilteredPosts(
            @Param("userId") String userId,
            @Param("receiverType") String receiverType,
            @Param("search") String search,
            Pageable page);

    @Query("SELECT DISTINCT p FROM Post p " +
            "WHERE p.user.id = :senderId " +
            "AND p.receiverType = :type " +
            "AND p.receiverId = :receiverId " +
            "AND CONCAT(p.content, p.title) LIKE %:filter% " +
            "ORDER BY p.lastUpdated DESC")
    Page<Post> getFilteredPostsToTypeWithId(@Param("type") String type,
                                            @Param("receiverId") String receiverId,
                                            @Param("senderId") String senderId,
                                            @Param("filter") String filter,
                                            Pageable page);

    @Query("SELECT DISTINCT p FROM Post p WHERE p.receiverType = :type AND p.receiverId = :receiverId AND CONCAT(p.content, p.title) LIKE %:filter% ORDER BY p.lastUpdated DESC")
    Page<Post> getFilteredPostsToTypeWithIdFromUser(@Param("type") String type,
                                                    @Param("receiverId") String receiverId,
                                                    @Param("filter") String filter,
                                                    Pageable page);



    @Query("SELECT DISTINCT p " +
            "FROM Post p INNER JOIN p.topic.users u " +
            "WHERE u.id = :senderId AND p.topic.id = :topicId " +
            "AND CONCAT(p.content, p.title) LIKE %:filter% " +
            "ORDER BY p.lastUpdated DESC")
    Page<Post> getFilteredPostsToTopic(@Param("topicId") long topicId,
                                       @Param("senderId") String userId,
                                       @Param("filter") String filter,
                                       Pageable page);

    Post findPostByIdOrderByLastUpdatedDesc(long postId);

}
