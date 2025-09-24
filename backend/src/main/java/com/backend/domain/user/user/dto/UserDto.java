package com.backend.domain.user.user.dto;

import com.backend.domain.user.user.entity.Users;
import java.time.LocalDateTime;


public record UserDto (
        Long userId,
        String userEmail,
        String phoneNumber,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        int level,
        String apiKey
) {

    public UserDto(Users users){
        this(
                users.getUserId(),
                users.getEmail(),
                users.getPhoneNumber(),
                users.getCreateDate(), // base Entity 도입시 해당 부분으로 객체 적용.
                users.getModifyDate(),
                users.getLevel(),
                users.getApiKey()
        );
    }
}
