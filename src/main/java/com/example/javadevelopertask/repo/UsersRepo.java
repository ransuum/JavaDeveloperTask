package com.example.javadevelopertask.repo;

import com.example.javadevelopertask.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UsersRepo extends JpaRepository<Users, UUID> {
    UserDetails findByEmail(String email);
}
