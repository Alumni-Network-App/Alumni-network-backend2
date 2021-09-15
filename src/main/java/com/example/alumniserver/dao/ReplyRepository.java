package com.example.alumniserver.dao;

import com.example.alumniserver.model.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Reply findReplyById(long id);
    @Query("SELECT r FROM Reply r WHERE r.user.id = :userId AND r.content LIKE %:searchTerm%")
    Page<Reply> findReplies(String userId, String searchTerm, Pageable page);
    List<Reply> findAllByPostId(long postId);
}
