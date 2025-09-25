package com.backend.domain.user.address.controller;

import com.backend.domain.user.address.dto.AddressDto;
import com.backend.domain.user.address.entity.Address;
import com.backend.domain.user.address.service.AddressService;
import com.backend.domain.user.user.dto.UserDto;
import com.backend.global.response.ApiResponse;
import com.backend.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/users/address")
@RequiredArgsConstructor
@RestController
@Tag(name = "Address", description = "주소 Api")
public class AddressController {
    private final AddressService addressService;
    private final Rq rq;

    record AddAddressReqBody(
        String address,
        String addressDetail,
        String postNumber
    ) {}


    @PostMapping("/add")
    @Operation(summary = "주소 등록", description = "주소를 저장합니다.")
    public ResponseEntity<ApiResponse<AddressDto>> addAddress(
            @RequestBody AddAddressReqBody addAddressReqBody
    ) throws Exception {
        AddressDto addressDto = new AddressDto(
                null,
                null,
                addAddressReqBody.address,
                addAddressReqBody.addressDetail,
                addAddressReqBody.postNumber
        );
        UserDto userDto = rq.getUser();
        addressService.addAddress(addressDto,userDto);

        return ResponseEntity.ok(ApiResponse.success(addressDto));
    }

    @GetMapping("/list")
    @Operation(summary = "주소 목록" , description = "사용자의 주소 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<AddressDto>>> getAllAddresses() throws Exception {
        UserDto userDto = rq.getUser();
        List<AddressDto> addressDtoList = addressService.getAllAddress(userDto);
        return ResponseEntity.ok(ApiResponse.success(addressDtoList));
    }


}
