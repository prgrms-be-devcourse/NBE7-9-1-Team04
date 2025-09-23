package com.backend.domain.user.user.dto;

import java.time.LocalDateTime;

record UserDto (
        Long userId,
        String userEmail,
        String password,
        String phoneNumber,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        int level
){
}
