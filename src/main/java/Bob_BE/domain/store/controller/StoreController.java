package Bob_BE.domain.store.controller;

import Bob_BE.domain.menu.dto.request.MenuRequestDto.MenuCreateRequestDto;
import Bob_BE.domain.menu.dto.response.MenuResponseDto.CreateMenuResponseDto;

import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.menu.service.MenuService;
import Bob_BE.domain.operatingHours.dto.request.OHRequestDto;
import Bob_BE.domain.operatingHours.dto.response.OHResponseDto;
import Bob_BE.domain.operatingHours.service.OperatingHoursService;
import Bob_BE.domain.owner.service.OwnerService;
import Bob_BE.domain.store.converter.StoreConverter;
import Bob_BE.domain.store.converter.StoreDtoConverter;
import Bob_BE.domain.store.dto.parameter.StoreParameterDto;
import Bob_BE.domain.store.dto.request.StoreRequestDto;
import Bob_BE.domain.store.dto.request.StoreRequestDto.StoreCreateRequestDto;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.service.StoreService;
import Bob_BE.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final MenuService menuService;
    private final OperatingHoursService operatingHoursService;
    private final OwnerService ownerService;
    
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


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "가게 등록 API", description = "가게 정보를 등록하는 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OWNER404", description = "유저 정보가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OAUTH401", description = "JWT 토큰 만료"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OAUTH404", description = "JWT 토큰 없음")
    })
    public ApiResponse<StoreResponseDto.StoreCreateResultDto> createStore(
            @RequestHeader(value = "Authorization",required = false) String authorizationHeader,
            @RequestPart("store") StoreRequestDto.StoreCreateRequestDto requestDto,
            @RequestPart(value = "bannerFiles", required = false) MultipartFile[] bannerFiles
    ){
        Long ownerId = ownerService.getOwnerIdFromJwt(authorizationHeader);
        StoreResponseDto.StoreCreateResultDto responseDto = storeService.createStore(ownerId, requestDto, bannerFiles);
        return ApiResponse.onSuccess(responseDto);
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

    @GetMapping("")
    @Operation(summary = "지도 핑을 위한 데이터 가져오기 API", description = "지도 핑을 위한 데이터 가져오기 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UNIVERSITY404", description = "해당 대학교를 찾지 못했습니다.")
    })
    @Parameters({
            @Parameter(name = "universityId", description = "대학교 식별자, RequestParam")
    })
    public ApiResponse<StoreResponseDto.GetDataForPingResponseDto> GetDataForPing (@RequestParam Long universityId) {

        StoreParameterDto.GetDataForPingParamDto getDataForPingParamDto = StoreDtoConverter.INSTANCE.toGetDataForPingParamDto(universityId);
        List<StoreResponseDto.StoreDataDto> storeDataDtoList = storeService.GetStoreDataList(getDataForPingParamDto);
        return ApiResponse.onSuccess(StoreConverter.toGetDataForPingResponseDto(storeDataDtoList));
    }

    @GetMapping("/{storeId}")
    @Operation(summary = "가게 상세 페이지 API", description = "가게 상세 페이지 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "STORE404", description = "해당 가게를 찾지 못했습니다.")
    })
    @Parameters({
            @Parameter(name = "storeId", description = "가게 식별자, PathVariable")
    })
    public ApiResponse<StoreResponseDto.GetStoreDataResponseDto> GetStoreDataList (@PathVariable("storeId") Long storeId) {

        StoreParameterDto.GetStoreDataParamDto getStoreDataParamDto = StoreDtoConverter.INSTANCE.toGetStoreDataParamDto(storeId);
        Store findStore = storeService.GetStoreData(getStoreDataParamDto);
        List<StoreResponseDto.GetStoreMenuDataDto> getStoreMenuDataDtoList = menuService.GetStoreMenuData(getStoreDataParamDto);
        return ApiResponse.onSuccess(StoreConverter.toGetStoreDataResponseDto(findStore, getStoreMenuDataDtoList));
    }

    @PostMapping("/{storeId}/operating_hours")
    @Operation(summary = "운영시간 등록 API", description = "가게 운영시간을 등록하는 API")
    public ApiResponse<OHResponseDto> createStoreOperatingHours(
            @PathVariable("storeId") Long storeId,
            @RequestBody List<OHRequestDto> requestDto){


        return ApiResponse.onSuccess(operatingHoursService.createOperatingHours(storeId, requestDto));
    }

    @PatchMapping("/{storeId}/operating_hours")
    @Operation(summary = "운영시간 수정 API", description = "가게 운영시간을 수정하는 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OWNER404", description = "가게 정보가 존재하지 않습니다.")

    })
    @Parameters({
            @Parameter(name = "storeId", description = "가게 Id")
    })
    public ApiResponse<OHResponseDto> updateStoreOperatingHours(
            @PathVariable("storeId") Long storeId,
            @RequestBody List<OHRequestDto> requestDto){

        return ApiResponse.onSuccess(operatingHoursService.updateOperatingHours(storeId, requestDto));
    }

}
