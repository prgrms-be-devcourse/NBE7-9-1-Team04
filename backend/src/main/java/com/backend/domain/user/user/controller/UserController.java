package com.backend.domain.user.user.controller;

import com.backend.domain.user.user.dto.UserDto;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.service.UserService;
import com.backend.global.response.ApiResponse;


import com.backend.global.rq.Rq;
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
@Tag(name = "User", description = "회원 API")
public class UserController {

    private final UserService userService;
    private final HttpServletResponse  httpServletResponse;
    private final Rq rq;

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
    @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다.")
    public ResponseEntity<ApiResponse> join(
            @Valid @RequestBody UserController.UserJoinReqBody form
    ) throws Exception {
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
    @Operation(summary = "회원 로그인", description = "사용자의 정보를 확인하고 ApiKey를 클라이언트에 전송합니다.")
    public ResponseEntity<ApiResponse> login(
            @Valid @RequestBody UserLoginReqBody reqBody
    ) throws Exception {

        UserDto userDto = userService.login(reqBody.email,reqBody.password);
        rq.setCookie("apiKey", userDto.apiKey());
        //TODO Cookie 수명 얼마로 잡을지 생각해보기.
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/logout")
    @Operation(summary = "회원 로그아웃", description = "현재 로그인 된 회원의 정보를 클라이언트에서 제거하여 로그아웃 시킵니다.")
    public ResponseEntity<ApiResponse> logout() throws Exception {
        if(userService.isApiKeyExists(rq.getCookieValue("apiKey",""))) rq.deleteCookie("apiKey");
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/my")
    @Operation(summary = "회원 정보 조회", description = "현재 로그인된 회원의 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<UserDto>> my() throws Exception {
        String apiKey= rq.getCookieValue("apiKey", "");
        UserDto userDto = userService.getUserByApiKey(apiKey);

        return new ResponseEntity<>(ApiResponse.success(userDto), HttpStatus.OK);
    }

    record UserModifyReqBody(
            @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 양식을 지켜주세요.")
            String phoneNumber
    ){}

    @PutMapping("/modify")
    @Operation(summary = "회원 정보 수정", description = "현재 로그인 된 회원의 정보를 수정합니다.")
    public ResponseEntity<ApiResponse<UserDto>> modify(
            @Valid @RequestBody UserModifyReqBody reqBody
    ) throws Exception {

        // 바꿀 정보라곤 전화번호 뿐이라 일단 이거라도 바꿉니다...
        String apiKey= rq.getCookieValue("apiKey", "");
        UserDto userDto = userService.modifyPhoneNumber(reqBody.phoneNumber, apiKey);
        return ResponseEntity.ok(ApiResponse.success(userDto));
    }

}
