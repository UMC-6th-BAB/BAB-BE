package Bob_BE.domain.store.controller;

import Bob_BE.domain.menu.dto.request.MenuRequestDto.MenuCreateRequestDto;
import Bob_BE.domain.menu.dto.response.MenuResponseDto.CreateMenuResponseDto;
import Bob_BE.domain.store.dto.request.StoreRequestDto;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.service.StoreService;
import Bob_BE.global.response.ApiResponse;
import java.util.List;

import Bob_BE.global.response.code.resultCode.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping("/{storeId}/menus")
    public ApiResponse<List<CreateMenuResponseDto>> createMenus(
            @PathVariable Long storeId,
            @RequestBody MenuCreateRequestDto requestDto
    ){
        var response = storeService.createMenus(storeId, requestDto);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/{owner_id}")
    @Operation(summary = "가게 등록 API", description = "가게 정보를 등록하는 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OWNER404", description = "사장님 정보가 등록되어 있지 않습니다.")

    })
    @Parameters({
            @Parameter(name = "ownerId", description = "사장님 Id")
    })
    public ApiResponse<StoreResponseDto.StoreCreateResultDto> createStore(@PathVariable("owner_id") Long ownerId, @RequestBody StoreRequestDto.StoreCreateRequestDto requestDto){


        return ApiResponse.onSuccess(storeService.createStore(ownerId, requestDto));
    }

}
