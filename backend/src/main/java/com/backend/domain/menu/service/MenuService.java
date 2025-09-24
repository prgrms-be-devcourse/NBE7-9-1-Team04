package com.backend.domain.menu.service;

import com.backend.domain.menu.dto.MenuAddRequest;
import com.backend.domain.menu.dto.MenuResponse;
import com.backend.domain.menu.entity.Menu;
import com.backend.domain.menu.repository.MenuRepository;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    // 메뉴 생성 (관리자)
    public MenuResponse createMenu(MenuAddRequest request) {

        // 관리자 권한 체크
//        if (user.getLevel() != 1) { // 예: 1 = 관리자
//            throw new BusinessException(ErrorCode.FORBIDDEN_ADMIN);
//        }

        // 메뉴 이름 중복 체크
        // 예외 처리 핸들러 추가 되면 변경 필요
        if (menuRepository.existsByName(request.name())) {
            throw new BusinessException(ErrorCode.DUPLICATE_MENU_NAME);
        }

        Menu menu = new Menu(
                request.name(),
                request.price(),
                request.description(),
                request.imageUrl()
        );

        return MenuResponse.from(menuRepository.save(menu));
    }

}
