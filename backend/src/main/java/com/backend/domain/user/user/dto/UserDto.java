package com.backend.domain.user.user.dto;

import com.backend.domain.user.user.entity.User;

import java.time.LocalDateTime;


record UserDto (
        Long userId,
        String userEmail,
        String password,
        String phoneNumber,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        int level
) {

    public UserDto(User user){
        this(
                user.getUserId(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getCreateDate(),
                user.getModifyDate(),
                user.getLevel()
        );
    }
}
