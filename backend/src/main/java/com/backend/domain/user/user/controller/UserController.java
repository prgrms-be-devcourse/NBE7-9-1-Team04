package com.backend.domain.user.user.controller;

import com.backend.domain.user.user.dto.UserDto;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.service.UserService;
import com.backend.global.response.ApiResponse;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
@Tag(name = "UserController", description = "")
public class UserController {

    private final UserService userService;
    private final HttpServletResponse  httpServletResponse;

    record UserJoinReqBody(
            @Email(message = "이메일 양식을 지켜서 입력해주세요.")
            @NotNull(message = "이메일을 입력해주세요.")
            @NotBlank(message = "이메일을 입력해주세요.")
            String email,
            @NotNull(message = "비밀번호를 입력해주세요.")
            @Size(min=8, max= 20, message = "비밀번호 길이는 8자 이상, 20자 이하입니다.")
            String password,
            @NotBlank(message = "전화번호를 입력해주세요.")
            @NotNull(message = "전화번호를 입력해주세요.")
            @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 양식을 지켜주세요.")
            String phoneNumber
    ) {}

    @PostMapping("/join")
    public ResponseEntity<ApiResponse> Join(
            @Valid @RequestBody UserController.UserJoinReqBody form,
            BindingResult bindingResult
    ) throws Exception {
        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessageBuilder = new StringBuilder();
            for(FieldError error : errors){
                errorMessageBuilder.append(error.getDefaultMessage());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(
                    "400",
                    errorMessageBuilder.toString(),
                    null
            ));
        }

        userService.createUser( form.email, form.password, form.phoneNumber);
        return ResponseEntity.ok(ApiResponse.success());
    }

    record UserLoginReqBody(
            @NotBlank
            @NotNull
            @Email
            String email,
            @NotBlank
            @Size(min=8, max = 20)
            String password
    )
    {}

    @GetMapping("/login")
    public ResponseEntity<ApiResponse> Login(
            @Valid @RequestBody UserLoginReqBody reqBody,
            BindingResult bindingResult
    ) throws Exception {
        //Todo Binding Result 처리 어떻게 할지
        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessageBuilder = new StringBuilder();
            for(FieldError error : errors){
                errorMessageBuilder.append(error.getDefaultMessage());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(
                    "400",
                    errorMessageBuilder.toString(),
                    null
            ));
        }

        UserDto userDto = userService.login(reqBody.email,reqBody.password);
        Cookie cookie = new Cookie("userId", userDto.userId()+"");
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> Logout(){
        Cookie cookie = new Cookie("userId", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(ApiResponse.success());
    }

}
