package com.example.alumniserver.dao;

import com.example.alumniserver.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Reply findReplyById(long id);
    List<Reply> findRepliesByUserId(String userId);
    List<Reply> findAllByPostId(long postId);
}
