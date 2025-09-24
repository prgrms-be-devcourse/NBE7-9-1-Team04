package com.backend.domain.user.user.controller;

import com.backend.domain.user.user.service.UserService;
import com.backend.global.response.ApiResponse;



import com.backend.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
@Tag(name = "UserController", description = "")
public class UserController {

    private final UserService userService;

    record UserJoinForm(
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
            @Valid @RequestBody UserJoinForm form,
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


}
