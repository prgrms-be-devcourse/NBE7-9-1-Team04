package com.backend.domain.user.user.service;

import com.backend.domain.user.user.dto.UserDto;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.repository.UserRepository;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ErrorCode;
import com.backend.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Users createUser(String email, String password, String phoneNumber) throws Exception {
        Optional<Users> optionalUsers= userRepository.getUsersByEmail(email);
        if(optionalUsers.isPresent()){
            throw new BusinessException(ErrorCode.CONFLICT_REGISTER);
        }


        Users newUsers = new Users(email,passwordEncoder.encode(password),phoneNumber,1);
        return userRepository.save(newUsers);
    }


    public UserDto login(String email, String password) throws Exception {
        Optional<Users> optionalUsers = userRepository.getUsersByEmail(email);
        if (!optionalUsers.isPresent()) {
            throw new BusinessException(ErrorCode.BAD_CREDENTIAL);
        }
        Users users = optionalUsers.get();

        if(!passwordEncoder.matches(password,users.getPassword())){
            throw new BusinessException(ErrorCode.BAD_CREDENTIAL);
        }

        return new UserDto(users);
    }

    public UserDto getUserByEmail(String email) throws Exception {
        Optional<Users> optionalUser =  userRepository.getUsersByEmail(email);
        if(!optionalUser.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND_MEMBER);
        }

        return new UserDto(optionalUser.get());
    }

    public UserDto getUserByUserId(Long userId) throws Exception {
        Optional<Users> optionalUser = userRepository.getUsersByUserId(userId);
        if(!optionalUser.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND_MEMBER);
        }

        return new UserDto(optionalUser.get());
    }

    public UserDto getUserByApiKey(String apikey) throws Exception {
        return new UserDto(findUserByApiKey(apikey));
    }

    public String changeApiKey(Users user) throws Exception {
        String newApiKey = user.changeApiKey();
        userRepository.save(user);
        return newApiKey;
    }

    public UserDto modifyPhoneNumber(String phoneNumber, String apiKey) throws Exception {
         Users user = findUserByApiKey(apiKey);
         user.changePhoneNumber(phoneNumber);
         userRepository.save(user);
         return new UserDto(user);
    }

    private Users findUserByApiKey(String apiKey) throws Exception {
        if(apiKey.isBlank()){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ACCESS);
        }

        Optional<Users> optionalUsers = userRepository.getUserByApiKey(apiKey);
        if(!optionalUsers.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND_MEMBER);
        }
        return optionalUsers.get();
    }

    public boolean isApiKeyExists(String apiKey) throws Exception {
        if(apiKey.isBlank()){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ACCESS);
        }
        Optional<Users> optionalUsers = userRepository.getUserByApiKey(apiKey);
        if(!optionalUsers.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND_MEMBER);
        }
        return true;
    }

}
