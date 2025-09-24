package com.backend.domain.menu.controller;

import com.backend.domain.menu.dto.MenuAddRequest;
import com.backend.domain.menu.dto.MenuResponse;
import com.backend.domain.menu.service.MenuService;
import com.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "MenuController", description = "메뉴 조회 및 관리자 메뉴 API")
public class MenuController {

    private final MenuService menuService;

    // ============ 관리자 ============

    @PostMapping("/api/admin/menu")
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @Valid @RequestBody MenuAddRequest request) {
        // TODO: 메뉴 생성 로직 구현
        MenuResponse response = menuService.createMenu(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
