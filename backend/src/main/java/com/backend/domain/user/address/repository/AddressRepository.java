package com.backend.domain.user.address.repository;

import com.backend.domain.user.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
