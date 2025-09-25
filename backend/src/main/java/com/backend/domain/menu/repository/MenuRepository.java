package com.backend.domain.menu.repository;

import com.backend.domain.menu.entity.Menu;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    boolean existsByName(String name);

    // 품절이 아닌 메뉴만 조회
    List<Menu> findByIsSoldOutFalse();

    boolean existsByNameAndMenuIdNot(String name, Long menuId);
}
