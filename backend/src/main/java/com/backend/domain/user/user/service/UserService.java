package com.backend.domain.user.user.service;

import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Users createUser(String email, String password, String phoneNumber) throws Exception {
        String[] phoneBits = phoneNumber.split("-");
        if(phoneBits.length != 3) {
            throw new Exception("Invalid phone number");
        }


        Users newUsers = new Users(email,password,phoneNumber,1);
        return userRepository.save(newUsers);
    }

}
