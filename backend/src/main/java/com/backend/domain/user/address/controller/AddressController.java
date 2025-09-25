package com.backend.domain.user.address.controller;

import com.backend.domain.user.address.dto.AddressDto;
import com.backend.domain.user.address.dto.request.AddressReqBody;
import com.backend.domain.user.address.service.AddressService;
import com.backend.domain.user.user.dto.UserDto;
import com.backend.global.response.ApiResponse;
import com.backend.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/users/address")
@RequiredArgsConstructor
@RestController
@Tag(name = "Address", description = "주소 Api")
public class AddressController {
    private final AddressService addressService;
    private final Rq rq;

    @PostMapping("/add")
    @Operation(summary = "주소 등록", description = "주소를 저장합니다.")
    public ResponseEntity<ApiResponse<AddressDto>> addAddress(
            @RequestBody AddressReqBody addressReqBody
    ) throws Exception {
        AddressDto addressDto = new AddressDto(addressReqBody);
        UserDto userDto = rq.getUser();
        addressService.addAddress(addressDto,userDto);

        return ResponseEntity.ok(ApiResponse.success(addressDto));
    }

    @GetMapping("/list")
    @Operation(summary = "주소 목록 조회" , description = "사용자의 주소 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<AddressDto>>> getAllAddresses() throws Exception {
        UserDto userDto = rq.getUser();
        List<AddressDto> addressDtoList = addressService.getAllAddress(userDto);
        return ResponseEntity.ok(ApiResponse.success(addressDtoList));
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "주소 개별 조회", description = "사용자의 개별 주소를 가져옵니다.")
    public ResponseEntity<ApiResponse<AddressDto>> getAddress(
            @PathVariable Long addressId
    ) throws  Exception {
        UserDto userDto = rq.getUser();
        AddressDto addressDto = addressService.getAddressById(addressId,userDto);
        return ResponseEntity.ok(ApiResponse.success(addressDto));
    }

    @PutMapping("/modify/{addressId}")
    @Operation(summary = "주소 갱신", description = "사용자의 주소를 갱신합니다. ")
    public ResponseEntity<ApiResponse<AddressDto>> modifyAddress(
            @PathVariable Long addressId,
            @RequestBody AddressReqBody reqBody
    ) throws Exception {
        UserDto userDto = rq.getUser();
        AddressDto addressDto = addressService.updateAddress(new AddressDto(reqBody),addressId,userDto);
        return ResponseEntity.ok(ApiResponse.success(addressDto));
    }

    @DeleteMapping("/delete/{addressId}")
    @Operation(summary = "주소 삭제" , description = "사용자의 해당 주소를 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteAddress(
            @PathVariable Long addressId
    ) throws Exception {
        UserDto userDto = rq.getUser();
        addressService.deleteAddress(addressId, userDto);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
