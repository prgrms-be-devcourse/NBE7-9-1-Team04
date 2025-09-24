package com.backend.domain.menu.controller;

import com.backend.domain.menu.dto.MenuAddRequest;
import com.backend.domain.menu.dto.MenuResponse;
import com.backend.domain.menu.service.MenuService;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.repository.UserRepository;
import com.backend.domain.user.user.service.UserService;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ApiResponse;
import com.backend.global.response.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "MenuController", description = "메뉴 조회 및 관리자 메뉴 API")
public class MenuController {

    private final MenuService menuService;
    private final UserRepository userRepository;

    // ============ 관리자 ============

    @PostMapping("/api/admin/menu")
    @Operation(summary = "메뉴 생성", description = "새로운 메뉴를 생성합니다. (관리자 전용)")
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @Valid @RequestBody MenuAddRequest request) {
        // TODO: 메뉴 생성 로직 구현

        MenuResponse response = menuService.createMenu(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
