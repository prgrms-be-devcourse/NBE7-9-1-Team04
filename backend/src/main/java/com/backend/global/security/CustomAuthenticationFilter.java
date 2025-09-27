package com.backend.global.security;

import com.backend.domain.user.address.service.AddressService;
import com.backend.domain.user.user.dto.UserDto;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.service.UserService;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ApiResponse;
import com.backend.global.response.ErrorCode;
import com.backend.global.rq.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final Rq rq;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("custom authentication filter called");

        try {
            authenticate(request, response, filterChain);
        } catch (BusinessException e) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(e.getErrorCode().getStatus().value());
            response.getWriter().write("""
                    {
                        "code" : "%s",
                        "message" : "%s"
                    }
                    """.formatted(e.getErrorCode().getCode(), e.getErrorCode().getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private void authenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws Exception {
        if (!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apikey;
        apikey = rq.getCookieValue("apiKey", "");

        if (apikey.isBlank()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ACCESS);
        }

        UserDto userDto = userService.getUserByApiKey(apikey);

        UserDetails user = new User(
                userDto.userEmail(),
                "",
                List.of()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword()
                , user.getAuthorities()
        );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }
}
