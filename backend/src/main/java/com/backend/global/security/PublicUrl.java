package com.backend.global.security;

import lombok.Data;
import lombok.Getter;

/** 로그인 하지 않아도 이용 가능한 URL들을 모아놓는 공간입니다. 외부에 공개할 URL을 작성해 주세요.**/
@Getter
public enum PublicUrl {
    // NAME_URL("path") 로 작성해주시면 됩니다.
    LOGIN_URL("/api/users/login"),
    JOIN_URL("/api/users/join")
    ;

    private final String path;
    PublicUrl(String url) {
        this.path = url;
    }
}
