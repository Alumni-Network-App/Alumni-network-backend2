package com.example.alumniserver.dao;

import com.example.alumniserver.model.Topic;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Topic findById(long id);

}
