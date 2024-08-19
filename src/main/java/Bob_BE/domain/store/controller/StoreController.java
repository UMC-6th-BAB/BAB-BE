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
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.service.StoreService;
import Bob_BE.domain.student.entity.Student;
import Bob_BE.domain.student.service.StudentService;
import Bob_BE.domain.university.entity.University;
import Bob_BE.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final MenuService menuService;
    private final OperatingHoursService operatingHoursService;
    private final OwnerService ownerService;
    private final StudentService studentService;

    @PostMapping("/{storeId}/menus")
    @Operation(summary = "메뉴 추가 API", description = "가게에 새로운 메뉴들을 추가하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "STORE 404", description = "해당하는 가게가 존재하지않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MENU 401", description = "유효하지 않은 시그니처 메뉴 개수입니다.")
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
            @RequestPart StoreRequestDto.StoreCreateRequestDto requestDto,
            @ModelAttribute(value = "bannerFile") MultipartFile bannerFile
    ){
        Long ownerId = ownerService.getOwnerIdFromJwt(authorizationHeader);
        StoreResponseDto.StoreCreateResultDto responseDto = storeService.createStore(ownerId, requestDto, bannerFile);
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "해당 학생을 찾지 못했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OWNER404", description = "해당 사장님을 찾지 못했습니다.."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OAUTH401", description = "JWT 토큰 만료"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OAUTH404", description = "JWT 토큰 없음")
    })
    public ApiResponse<StoreResponseDto.GetOnSaleStoreListResponseDto> GetOnSaleStoreList(
            @RequestHeader(value = "Authorization",required = false) String authorizationHeader) {

        StoreParameterDto.GetOnSaleStoreListParamDto getOnSaleStoreListParamDto = StoreDtoConverter.INSTANCE.toGetOnSaleStoreListParamDto(authorizationHeader);
        List<StoreResponseDto.GetOnSaleStoreDataDto> getOnSaleStoreDataDtoList = storeService.GetOnSaleStoreListData(getOnSaleStoreListParamDto);
        return ApiResponse.onSuccess(StoreConverter.toGetOnSaleStoreListResponseDto(getOnSaleStoreDataDtoList));
    }

    @GetMapping("")
    @Operation(summary = "지도 핑을 위한 데이터 가져오기 API", description = "지도 핑을 위한 데이터 가져오기 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "해당 유저를 찾지 못했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OAUTH401", description = "JWT 토큰 만료"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OAUTH404", description = "JWT 토큰 없음")
    })
    public ApiResponse<StoreResponseDto.GetDataForPingResponseDto> GetDataForPing (
            @RequestHeader(value = "Authorization",required = false) String authorizationHeader) {

        StoreParameterDto.GetDataForPingParamDto getDataForPingParamDto = StoreDtoConverter.INSTANCE.toGetDataForPingParamDto(authorizationHeader);
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
        List<StoreResponseDto.StoreMenuData> storeMenuDataList = menuService.GetStoreMenuData(getStoreDataParamDto);
        return ApiResponse.onSuccess(StoreConverter.toGetStoreDataResponseDto(findStore, storeMenuDataList));
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


    @PostMapping(value = "/certificates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "사업자 등록증 등록 API", description = "사업자 등록증 정보를 불러오는 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CERTIFICATE404", description = "정보를 읽어오는데 실패했습니다.")
    })
    public ApiResponse<StoreResponseDto.CertificateResultDto> registerCertificates(@RequestPart("file") MultipartFile file) throws IOException {

        return ApiResponse.onSuccess(storeService.registerCertificates(file));
    }

    @GetMapping("/menus/search")
    public ApiResponse<List<StoreResponseDto.GetStoreSearchDto>> searchStores(
            @RequestParam String keyword,
            @RequestParam Long studentId,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude
    ) {
        Student student = studentService.findStudentById(studentId);
        StoreParameterDto.GetSearchKeywordParamDto searchKeywordParamDto = StoreParameterDto.GetSearchKeywordParamDto.builder()
                .keyword(keyword)
                .build();

        List<StoreResponseDto.GetStoreSearchDto> stores;

        // 위도와 경도가 제공된 경우
        if (latitude != null && longitude != null) {
            stores = storeService.searchStoreWithMenusByCoordinates(searchKeywordParamDto, latitude, longitude);
        } else {
            // 위도와 경도가 없을 때 기존 로직 사용
            stores = storeService.searchStoreWithMenus(searchKeywordParamDto, student);
        }

        return ApiResponse.onSuccess(stores);
    }
    @GetMapping("/{storeId}/inform")
    @Operation(summary = "가게정보 가져오기 API", description = "가게의 정보를 가져오며 배너가 없는 경우 null을 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "STORE404", description = "해당 가게를 찾지 못했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "STOREUNIVERSITY401", description = "가게와 연결된 대학교가 존재하지 않습니다.")
    })
    public ApiResponse<StoreResponseDto.StoreInformDto> getStoreInform(@PathVariable(name = "storeId")Long storeId){
        Store store = storeService.getStore(storeId);
        String bannerUrl = storeService.getStoreBannerUrl(store);
        University university = storeService.getStoreUniversity(store);

        return ApiResponse.onSuccess(StoreConverter.toStoreInformDto(store, bannerUrl, university));
    }

}
