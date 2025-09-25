package com.backend.domain.user.user.repository;

import com.backend.domain.user.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> getUsersByEmail(String email);
    Optional<Users> getUserByApiKey(String apikey);
    Optional<Users> getUsersByUserId(Long userId);
}
