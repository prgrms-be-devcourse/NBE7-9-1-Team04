package com.backend.domain.menu.service;

import com.backend.domain.menu.dto.MenuAddRequest;
import com.backend.domain.menu.dto.MenuResponse;
import com.backend.domain.menu.entity.Menu;
import com.backend.domain.menu.repository.MenuRepository;
import com.backend.domain.user.user.entity.User;
import com.backend.global.response.ApiResponse;
import com.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    // 메뉴 생성 (관리자)
    public MenuResponse createMenu(MenuAddRequest request) {

        //관리자 권한 체크(추후 JWT 등 인증 구현 시 변경 필요)
//        if (user.getLevel() != 1) { // 예: 1 = 관리자
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자만 메뉴 생성 가능");
//        }

        // 메뉴 이름 중복 체크
        // 예외 처리 핸들러 추가 되면 변경 필요
        if (menuRepository.existsByName(request.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 메뉴 이름입니다.");
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
