package com.example.alumniserver.dao;

import com.example.alumniserver.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findGroupsByIsPrivate(boolean isPrivate);
    Group findGroupById(long id);
}
