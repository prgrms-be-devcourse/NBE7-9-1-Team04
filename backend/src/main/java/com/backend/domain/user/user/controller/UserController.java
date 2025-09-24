package com.backend.domain.user.user.controller;

import com.backend.domain.user.user.service.UserService;
import com.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
@Tag(name = "Controller 이름", description = "Controller 설명")
public class UserController {
    private final UserService userService;

    record UserJoinForm(
            @Email
            String email,
            @Size(min=8, max= 20)
            String password,
            String phoneNumber
    )
    {}

    @PostMapping("/join")
    public ApiResponse Join(
            @Valid UserJoinForm form
    ) throws Exception {
        userService.createUser(form.email,form.password, form.phoneNumber);
        return null;
    }
}
