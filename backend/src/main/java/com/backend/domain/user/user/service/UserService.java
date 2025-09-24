package com.backend.domain.user.user.service;

import com.backend.domain.user.user.entity.User;
import com.backend.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String email, String password,String phoneNumber) throws Exception {
        String[] phoneBits = phoneNumber.split("-");
        if(phoneBits.length != 3){
            throw new Exception("이후 예외부분 추가");
        }

        User newUser = new User(email,password,phoneNumber,1);
        return userRepository.save(newUser);
    }

}
