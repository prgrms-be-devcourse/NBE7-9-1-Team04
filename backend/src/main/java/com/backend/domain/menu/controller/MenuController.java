package com.backend.domain.menu.controller;

import com.backend.domain.menu.dto.MenuAddRequest;
import com.backend.domain.menu.dto.MenuResponse;
import com.backend.domain.menu.dto.MenuUpdateRequest;
import com.backend.domain.menu.service.MenuService;
import com.backend.domain.user.user.dto.UserDto;
import com.backend.domain.user.user.service.UserService;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ApiResponse;
import com.backend.global.response.ErrorCode;
import com.backend.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
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
    private final Rq rq;

    private void validateAdmin(UserDto actor)
    {
        if(actor.level() != 1)          //아직 관리자 레벨을 정하지 않아 임시로 1로 지정
        {
            throw new BusinessException(ErrorCode.FORBIDDEN_ADMIN);
        }
    }

    // =========== 사용자 ============

    @GetMapping("/api/menu")
    @Operation(summary = "메뉴 조회", description = "전체 메뉴(품절 제외)를 조회합니다. (사용자 전용)")
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getAllMenus() {
        // TODO: 메뉴 조회 로직 구현
        return ResponseEntity.ok(menuService.getAllMenu());
    }

    // ============ 관리자 ============

    @PostMapping("/api/admin/menu")
    @Operation(summary = "메뉴 생성", description = "새로운 메뉴를 생성합니다. (관리자 전용)")
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @Valid @RequestBody MenuAddRequest request) throws Exception {
        UserDto actor = rq.getUser();
        validateAdmin(actor);

        // TODO: 메뉴 생성 로직 구현
        MenuResponse response = menuService.createMenu(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/api/admin/menu")
    @Operation(summary = "메뉴 조회 (관리자)", description = "품절 여부와 상관없이 전체 메뉴를 조회합니다.")
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getAllMenusForAdmin() throws Exception {
        UserDto actor = rq.getUser();
        validateAdmin(actor);
        // TODO: 메뉴 조회 로직 구현
        return ResponseEntity.ok(menuService.getAllMenuForAdmin());
    }

    @GetMapping("api/admin/menu/{menuId}")
    @Operation(summary = "메뉴 상세 조회 (관리자)", description = "특정 메뉴의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<MenuResponse>> getMenuById(
            @PathVariable Long menuId
    ) throws Exception {
        UserDto actor = rq.getUser();
        validateAdmin(actor);
        // TODO: 메뉴 상세 조회 로직 구현
        MenuResponse response = menuService.getMenuById(menuId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/api/admin/menu/{menuId}")
    @Operation(summary = "메뉴 수정", description = "관리자가 특정 메뉴 정보를 수정합니다.")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequest request
    ) throws Exception {
        UserDto actor = rq.getUser();
        validateAdmin(actor);
        // TODO: 메뉴 수정 로직 구현
        MenuResponse response = menuService.updateMenu(menuId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/api/admin/menu/{menuId}")
    @Operation(summary = "메뉴 삭제", description = "관리자가 특정 메뉴를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(
            @PathVariable Long menuId
    ) throws Exception {
        UserDto actor = rq.getUser();
        validateAdmin(actor);
        // TODO: 메뉴 삭제 로직 구현
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
