package Bob_BE.domain.store.controller;

import Bob_BE.domain.menu.dto.request.MenuRequestDto.MenuCreateRequestDto;
import Bob_BE.domain.menu.dto.response.MenuResponseDto.CreateMenuResponseDto;

import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.menu.service.MenuService;
import Bob_BE.domain.store.converter.StoreConverter;
import Bob_BE.domain.store.converter.StoreDtoConverter;
import Bob_BE.domain.store.dto.parameter.StoreParameterDto;
import Bob_BE.domain.store.dto.request.StoreRequestDto;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.service.StoreService;
import Bob_BE.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final MenuService menuService;

    @PostMapping("/{storeId}/menus")
    @Operation(summary = "메뉴 추가 API", description = "가게에 새로운 메뉴들을 추가하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "STORE 404", description = "해당하는 가게가 존재하지않습니다.")
    })
    @Parameters({
            @Parameter(name = "storeId", description = "가게 식별자, PathVariable")
    })
    public ApiResponse<List<CreateMenuResponseDto>> createMenus(
            @PathVariable Long storeId,
            @RequestBody @Valid MenuCreateRequestDto requestDto
    ){
        var response = storeService.createMenus(storeId, requestDto);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/{storeId}/menus-name")
    @Operation(summary = "가게 메뉴 이름 목록 가져오기 API", description = "가게 메뉴 이름 목록 가져오기 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "STORE404", description = "해당 가게가 존재하지 않습니다.")

    })
    @Parameters({
            @Parameter(name = "storeId", description = "가게 식별자, PathVariable")
    })
    public ApiResponse<StoreResponseDto.GetMenuNameListResponseDto> GetMenuNameList (@PathVariable("storeId") Long storeId) {

        StoreParameterDto.GetMenuNameListParamDto getMenuNameListParamDto = StoreDtoConverter.INSTANCE.toGetMenuNameListParamDto(storeId);
        List<Menu> menuList = menuService.GetMenuListByStore(getMenuNameListParamDto);
        return ApiResponse.onSuccess(StoreConverter.toGetMenuNameListResponseDto(menuList));
    }


    @PostMapping("/{ownerId}")
    @Operation(summary = "가게 등록 API", description = "가게 정보를 등록하는 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OWNER404", description = "사장님 정보가 등록되어 있지 않습니다.")

    })
    @Parameters({
            @Parameter(name = "ownerId", description = "사장님 Id")
    })
    public ApiResponse<StoreResponseDto.StoreCreateResultDto> createStore(@PathVariable("ownerId") Long ownerId, @RequestBody StoreRequestDto.StoreCreateRequestDto requestDto){


        return ApiResponse.onSuccess(storeService.createStore(ownerId, requestDto));
    }

    @PatchMapping("/{storeId}")
    @Operation(summary = "가게 수정 API", description = "가게 정보를 수정하는 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OWNER404", description = "가게 정보가 존재하지 않습니다.")

    })
    @Parameters({
            @Parameter(name = "storeId", description = "가게 Id")
    })
    public ApiResponse<StoreResponseDto.StoreUpdateResultDto> updateStore(
            @PathVariable("storeId") Long storeId,
            @RequestBody StoreRequestDto.StoreUpdateRequestDto requestDto){

        return ApiResponse.onSuccess(storeService.updateStore(storeId, requestDto));
    }

    @DeleteMapping("/{storeId}")
    @Operation(summary = "가게 삭제 API", description = "가게를 삭제하는 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OWNER404", description = "가게 정보가 존재하지 않습니다.")

    })
    @Parameters({
            @Parameter(name = "storeId", description = "가게 Id")
    })
    public ApiResponse<StoreResponseDto.StoreDeleteResultDto> deleteStore(@PathVariable("storeId") Long storeId){

        return ApiResponse.onSuccess(storeService.deleteStore(storeId));
    }


    @GetMapping("/discounts")
    @Operation(summary = "오늘의 할인 가게 페이지 API", description = "오늘의 할인 가게 페이지 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UNIVERSITY404", description = "해당 대학교를 찾지 못했습니다.")
    })
    @Parameters({
            @Parameter(name = "universityId", description = "대학교 식별자, RequestParam")
    })
    public ApiResponse<StoreResponseDto.GetOnSaleStoreListResponseDto> GetOnSaleStoreList (@RequestParam Long universityId) {

        StoreParameterDto.GetOnSaleStoreListParamDto getOnSaleStoreListParamDto = StoreDtoConverter.INSTANCE.toGetOnSaleStoreListParamDto(universityId);
        List<StoreResponseDto.GetOnSaleStoreDataDto> getOnSaleStoreDataDtoList = storeService.GetOnSaleStoreListData(getOnSaleStoreListParamDto);
        return ApiResponse.onSuccess(StoreConverter.toGetOnSaleStoreListResponseDto(getOnSaleStoreDataDtoList));
    }
}
