package com.backend.domain.cart.repository;

import com.backend.domain.cart.entity.Cart;
import com.backend.domain.menu.entity.Menu;
import com.backend.domain.user.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserAndMenu(Users user, Menu menu);
}
