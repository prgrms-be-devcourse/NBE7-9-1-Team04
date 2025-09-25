package com.backend.domain.user.address.service;

import com.backend.domain.user.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;

}
