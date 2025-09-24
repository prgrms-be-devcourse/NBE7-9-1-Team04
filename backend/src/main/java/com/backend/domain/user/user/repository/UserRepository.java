package com.backend.domain.user.user.repository;

import com.backend.domain.user.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {
}
