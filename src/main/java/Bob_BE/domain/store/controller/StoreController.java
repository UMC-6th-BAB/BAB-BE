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
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final MenuService menuService;

    @PostMapping("/{storeId}/menus")
    public ApiResponse<List<CreateMenuResponseDto>> createMenus(
            @PathVariable Long storeId,
            @RequestBody MenuCreateRequestDto requestDto
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
