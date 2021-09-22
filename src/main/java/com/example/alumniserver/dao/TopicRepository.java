package com.example.alumniserver.dao;

import com.example.alumniserver.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("SELECT t FROM Topic t WHERE t.name LIKE %:name% ORDER BY t.lastUpdated DESC NULLS LAST")
    Page<Topic> findTopics(@Param("name")String name, Pageable page);

}
