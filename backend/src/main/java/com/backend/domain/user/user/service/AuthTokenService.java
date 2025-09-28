package com.backend.domain.user.user.service;

import com.backend.domain.user.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {

    @Value("${custom.jwt.secretPattern}")
    private String secretPatter;
    @Value("${custom.jwt.expireSeconds}")
    private int expireSeconds;

    String generateJwtToken(UserDto userDto){
        return null;
    }


}
