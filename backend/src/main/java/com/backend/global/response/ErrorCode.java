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
    EMPTY_CART("C006", HttpStatus.NOT_FOUND, "장바구니가 비어 있습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;

    ErrorCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}

