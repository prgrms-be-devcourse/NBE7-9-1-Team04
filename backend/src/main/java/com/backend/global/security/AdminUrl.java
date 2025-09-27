package com.backend.global.security;

/** 관리자만 접근 가능한 URL을 모아둡니다. **/
public enum AdminUrl {
    // NAME_URL("path") 로 작성해주시면 됩니다.
    // EXAMPLE_ULR("/admin/example")
    ;

    private final String path;
    AdminUrl(String url) {
        this.path = url;
    }
}
