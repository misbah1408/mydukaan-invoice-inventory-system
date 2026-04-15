package com.mydukaan.repository;

import com.mydukaan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User as u WHERE u.email = :email")
    Optional<User> findByEmail(String email);
}
