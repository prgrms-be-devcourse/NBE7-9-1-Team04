package com.backend.global.rq;

import com.backend.domain.user.user.dto.UserDto;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.service.UserService;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Rq {
    private final UserService userService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public UserDto getUser() throws Exception {
        String apiKey;

        apiKey = getCookieValue("apiKey","");
        if(apiKey.isBlank()){
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        }

        return userService.getUserByApiKey(apiKey);
    }

    public String getCookieValue(String name, String defaultValue) {
        return Optional
                .ofNullable(request.getCookies())
                .flatMap(cookies ->
                        Arrays.stream(cookies)
                                .filter(cookie -> cookie.getName().equals(name))
                                .map(Cookie::getValue)
                                .filter(value -> !value.isBlank())
                                .findFirst()
                )
                .orElse(defaultValue);
    }

    public String getHeader(String name, String defaultValue) {
        return Optional
                .ofNullable(request.getHeader(name))
                .filter(headerValue -> !headerValue.isBlank())
                .orElse(defaultValue);
    }

    public void setHeader(String name, String newAccessToken) {
        response.setHeader(name,newAccessToken);
    }

    public void setCookie(String name, String value) {
        if (value == null) value = "";

        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");

        // 값이 없다면 해당 쿠키변수를 삭제하라는 뜻
        if (value.isBlank()) {
            cookie.setMaxAge(0);
        }

        response.addCookie(cookie);
    }

    public void deleteCookie(String name) {
        setCookie(name, null);
    }
}
