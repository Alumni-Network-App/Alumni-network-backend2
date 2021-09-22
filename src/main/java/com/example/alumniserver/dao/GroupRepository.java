package com.example.alumniserver.dao;

import com.example.alumniserver.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findGroupById(long id);

    @Query("SELECT g FROM Group g INNER JOIN g.users u WHERE g.name LIKE %:name% AND (g.isPrivate = FALSE OR u.id = :userId) ORDER BY g.lastUpdated DESC NULLS LAST")
    Page<Group> findGroups(@Param("userId")String userId, @Param("name")String name, Pageable pageable);
}
