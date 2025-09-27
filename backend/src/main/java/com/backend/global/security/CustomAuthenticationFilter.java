package com.backend.global.security;

import com.backend.domain.user.address.service.AddressService;
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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final Rq rq;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("custom authentication filter called");

        try{
            authenticate(request,response,filterChain);
        } catch (BusinessException e){
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(e.getErrorCode().getStatus().value());
            response.getWriter().write("""
                    {
                        "code" : "%s",
                        "message" : "%s"
                    }
                    """.formatted(e.getErrorCode().getCode(),e.getErrorCode().getMessage()));
        }

    }


    private void authenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if(List.of("/api/users/join","/api/users/login").contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String apikey;
        apikey = rq.getCookieValue("apiKey","");

        if(apikey.isBlank()){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ACCESS);
        }

        filterChain.doFilter(request, response);

    }
}
