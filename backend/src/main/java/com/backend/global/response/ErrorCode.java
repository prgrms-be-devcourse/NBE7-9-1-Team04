package com.backend.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/** * 도메인별 발생할 Error Code 담당 enum */
@Getter
public enum ErrorCode {

    // 회원
    CONFLICT_REGISTER("U001", HttpStatus.CONFLICT, "이미 가입된 회원입니다."),
    NOT_FOUND_MEMBER("U002", HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    BAD_CREDENTIAL("U004", HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 틀렸습니다."),
    INVALID_TOKEN("U005", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("U006", HttpStatus.UNAUTHORIZED, "엑세스 토큰이 만료되었습니다. 토큰을 갱신해주세요."),

    // 장바구니
    // 장바구니에 추가하려는 상품 ID가 DB에 존재하지 않을 때 사용합니다.
    NOT_FOUND_PRODUCT("C001", HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    // 상품의 재고가 주문하려는 수량보다 적을 때 발생시킵니다.
    OUT_OF_STOCK("C002", HttpStatus.CONFLICT, "상품의 재고가 부족합니다."),
    // 수량을 0 이하의 값으로 변경하려고 할 때 발생시킵니다.
    INVALID_QUANTITY("C004", HttpStatus.BAD_REQUEST, "상품 수량은 1개 이상이어야 합니다."),
    // 장바구니가 비어있는 상태에서 주문 시도할 때 발생시킵니다.
    EMPTY_CART("C006", HttpStatus.NOT_FOUND, "장바구니가 비어 있습니다."),

    // 메뉴
    // 이름이 중복되는 메뉴를 생성하려고 할 때 발생시킵니다.
    DUPLICATE_MENU_NAME("M001", HttpStatus.CONFLICT, "이미 존재하는 메뉴 이름입니다."),
    // 메뉴 ID가 DB에 존재하지 않을 때 사용합니다.
    NOT_FOUND_MENU("M002", HttpStatus.NOT_FOUND, "존재하지 않는 메뉴입니다."),
    // 관리자 권한이 없는 사용자가 관리자 전용 기능을 사용하려고 할 때 발생시킵니다.
    FORBIDDEN_ADMIN("M003", HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),
    // 메뉴가 품절 상태일 때 발생시킵니다.
    MENU_SOLD_OUT("M004", HttpStatus.CONFLICT, "품절된 메뉴입니다."),
    // 메뉴 가격이 음수일 때 발생시킵니다.
    INVALID_MENU_PRICE("M005", HttpStatus.BAD_REQUEST, "메뉴 가격은 음수일 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;

    ErrorCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}

