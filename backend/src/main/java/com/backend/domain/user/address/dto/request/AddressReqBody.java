package com.backend.domain.user.address.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddressReqBody(
        @Schema(description = "주소" , example = "시험도 시험시 시험면")
        String address,
        @Schema(description = "상세 주소", example = "시험로 12-3길 12")
        String addressDetail,
        @Schema(description = "우편 번호", example = "12312")
        String postNumber
) {
}
