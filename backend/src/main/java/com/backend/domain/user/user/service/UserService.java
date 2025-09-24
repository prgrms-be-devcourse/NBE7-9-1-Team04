package com.backend.domain.user.user.service;

import com.backend.domain.user.user.dto.UserDto;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Users createUser(String email, String password, String phoneNumber) throws Exception {

        Users newUsers = new Users(email,password,phoneNumber,1);
        return userRepository.save(newUsers);
    }


    public UserDto login(String email, String password) throws Exception {
        Optional<Users> optionalUsers = userRepository.getUsersByEmail(email);
        if (!optionalUsers.isPresent()) {
            throw new Exception("User not found");
        }
        Users users = optionalUsers.get();

        if(!users.isMatchedPassword(password)){
            throw new Exception("Wrong password");
        }

        return new UserDto(users);
    }

    public UserDto getUserByEmail(String email) throws Exception {
        Optional<Users> optionalUser =  userRepository.getUsersByEmail(email);
        if(!optionalUser.isPresent()){
            throw new Exception("User not found");
        }

        return new UserDto(optionalUser.get());
    }

    public UserDto getUserByUserId(Long userId) throws Exception {
        Optional<Users> optionalUsers = userRepository.getUsersByUserId(userId);
        if(!optionalUsers.isPresent()){
            throw new Exception("User not found");
        }

        return new UserDto(optionalUsers.get());
    }
}
