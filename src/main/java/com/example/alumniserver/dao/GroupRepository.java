package com.example.alumniserver.dao;

import com.example.alumniserver.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
