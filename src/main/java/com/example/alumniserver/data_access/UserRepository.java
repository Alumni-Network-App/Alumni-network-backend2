package com.example.alumniserver.data_access;

import com.example.alumniserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
