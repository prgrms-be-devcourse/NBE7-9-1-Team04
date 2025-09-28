package com.backend.domain.user.user.dto;

import com.backend.domain.user.user.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    public Users getIdUser(){
        //ID값만 가지고 있는 User 객체를 사용
        return new Users(this);
    }
}
