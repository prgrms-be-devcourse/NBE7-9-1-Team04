package com.backend.domain.menu.repository;

import com.backend.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    boolean existsByName(String name);
}
