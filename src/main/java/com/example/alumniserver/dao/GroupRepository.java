package com.example.alumniserver.dao;

import com.example.alumniserver.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findGroupByGroupId(long id);

    @Query("SELECT g FROM Group g LEFT JOIN g.users u WHERE g.name LIKE %:name% AND (g.isPrivate = FALSE OR size(g.users) > 0 AND u.id = :userId) ORDER BY g.lastUpdated DESC NULLS LAST")
    Page<Group> findGroups(@Param("userId")String userId, @Param("name")String name, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Group g SET g.lastUpdated = :lastUpdated WHERE g.groupId = :id")
    void updateGroup(@Param(value = "id") long id, @Param(value = "lastUpdated") LocalDateTime lastUpdated);

}
