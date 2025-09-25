package com.backend.domain.user.address.service;

import com.backend.domain.user.address.dto.AddressDto;
import com.backend.domain.user.address.entity.Address;
import com.backend.domain.user.address.repository.AddressRepository;
import com.backend.domain.user.user.dto.UserDto;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.repository.UserRepository;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AddressService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public AddressDto addAddress(AddressDto addressDto, UserDto userDto) {
        Users user = userRepository.getUsersByUserId(userDto.userId()).get();

        Address address = new Address(
                user,
                addressDto.addressDetail(),
                addressDto.address(),
                addressDto.postNumber()
        );

        address = user.addAddress(address);
        return new AddressDto(address);
    }

    public List<AddressDto> getAllAddress(UserDto userDto) {
        Users user = userRepository.getUsersByUserId(userDto.userId()).get();

        List<Address> addresses = user.getAddresses();
        List<AddressDto> addressDtoList = addresses.stream().map(AddressDto::new).toList();

        return addressDtoList;
    }

    public AddressDto getAddressById(Long addressId, UserDto userDto) {
        Users user = userRepository.getUsersByUserId(userDto.userId()).get();

        Optional<Address> optionalAddress = user.getAddress(addressId);
        if (!optionalAddress.isPresent()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ADDRESS);
        }

        return new AddressDto(optionalAddress.get());
    }

    @Transactional
    public AddressDto updateAddress(AddressDto addressDto, Long addressId, UserDto userDto) {
        Users user = userRepository.getUsersByUserId(userDto.userId()).get();

        Optional<Address> optionalAddress = user.getAddress(addressId);
        if (!optionalAddress.isPresent()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ADDRESS);
        }

        Address address = optionalAddress.get();
        address.changeAddress(addressDto);

        return new AddressDto(address);
    }

    @Transactional
    public void deleteAddress(Long addressId, UserDto userDto) {
        Users user = userRepository.getUsersByUserId(userDto.userId()).get();
        Optional<Address> optionalAddress = user.getAddress(addressId);
        if (!optionalAddress.isPresent()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ADDRESS);
        }
        Address address = optionalAddress.get();
        user.deleteAddress(address);
    }
}
