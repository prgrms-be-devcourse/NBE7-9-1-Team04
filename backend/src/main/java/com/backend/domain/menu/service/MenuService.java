package com.backend.domain.menu.service;

import com.backend.domain.menu.dto.MenuAddRequest;
import com.backend.domain.menu.dto.MenuResponse;
import com.backend.domain.menu.dto.MenuUpdateRequest;
import com.backend.domain.menu.entity.Menu;
import com.backend.domain.menu.repository.MenuRepository;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ApiResponse;
import com.backend.global.response.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    // =========== 사용자 ============

    // 전체 메뉴 조회(사용자)
    public ApiResponse<List<MenuResponse>> getAllMenu() {
        List<MenuResponse> menus = menuRepository.findByIsSoldOutFalse().stream()
                .map(MenuResponse::from)
                .toList();

        return ApiResponse.success(menus);
    }

    // ============ 관리자 ============

    // 메뉴 생성 (관리자)
    public MenuResponse createMenu(MenuAddRequest request) {

        // 메뉴 이름 중복 체크
        if (menuRepository.existsByName(request.name())) {
            throw new BusinessException(ErrorCode.DUPLICATE_MENU_NAME);
        }

        Menu menu = request.toEntity();
        return MenuResponse.from(menuRepository.save(menu));
    }

    // 관리자 전용 조회 (품절 포함)
    public ApiResponse<List<MenuResponse>> getAllMenuForAdmin() {
        List<MenuResponse> menus = menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .toList();

        return ApiResponse.success(menus);
    }

    // 메뉴 수정 (관리자)
    public MenuResponse updateMenu(Long menuId, @Valid MenuUpdateRequest request) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        // 메뉴 이름 중복 체크 (자기 자신 제외)
        if (menuRepository.existsByNameAndMenuIdNot(request.name(), menuId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_MENU_NAME);
        }

        request.applyTo(menu); // DTO → 엔티티 갱신
        return MenuResponse.from(menuRepository.save(menu));
    }

    // 메뉴 상세 조회 (관리자)
    public MenuResponse getMenuById(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        return MenuResponse.from(menu);
    }

    // 메뉴 삭제 (관리자)
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        menuRepository.delete(menu);
    }
}
