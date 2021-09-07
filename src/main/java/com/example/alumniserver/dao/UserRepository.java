package com.example.alumniserver.dao;

import com.example.alumniserver.model.Post;
import com.example.alumniserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(long id);
}
