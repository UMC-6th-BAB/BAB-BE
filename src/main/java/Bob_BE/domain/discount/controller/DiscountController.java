package Bob_BE.domain.discount.controller;

import Bob_BE.domain.discount.converter.DiscountConverter;
import Bob_BE.domain.discount.converter.DiscountDtoConverter;
import Bob_BE.domain.discount.dto.parameter.DiscountParameterDto;
import Bob_BE.domain.discount.dto.request.DiscountRequestDto;
import Bob_BE.domain.discount.dto.response.DiscountResponseDto;
import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.discount.service.DiscountService;
import Bob_BE.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores")
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping("/{storeId}/menus/discounts")
    @Operation(summary = "할인 추가 API", description = "할인 추가 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "STORE404", description = "해당 가게가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MENU404", description = "해당 메뉴가 존재하지 않습니다.")

    })
    @Parameters({
            @Parameter(name = "storeId", description = "가게 식별자, PathVariable")
    })
    public ApiResponse<DiscountResponseDto.CreateDiscountResponseDto> CreateDiscount(@RequestBody @Valid DiscountRequestDto.CreateDiscountRequestDto request, @PathVariable("storeId") Long storeId) {

        DiscountParameterDto.CreateDiscountParamDto createDiscountParamDto = DiscountDtoConverter.INSTANCE.toCreateDiscountParamDto(request, storeId);
        Discount discount = discountService.CreateDiscount(createDiscountParamDto);
        return ApiResponse.onSuccess(DiscountConverter.toCreateDiscountResponseDto(discount));
    }

    @DeleteMapping("/{storeId}/menus/discounts/{discountId}")
    @Operation(summary = "할인 삭제 API", description = "할인 삭제 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "STORE404", description = "해당 가게가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DISCOUNT401", description = "해당 할인 행사가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DISCOUNT402", description = "해당 가게의 할인 행사가 아닙니다.")
    })
    @Parameters({
            @Parameter(name = "storeId", description = "가게 식별자, PathVariable"),
            @Parameter(name = "discountId", description = "할인 식별자, PathVariable")
    })
    public ApiResponse<DiscountResponseDto.DeleteDiscountResponseDto> DeleteDiscount(@PathVariable("storeId") Long storeId, @PathVariable("discountId") Long discountId) {

        DiscountParameterDto.DeleteDiscountParamDto deleteDiscountParamDto = DiscountDtoConverter.INSTANCE.toDeleteDiscountParamDto(storeId, discountId);
        discountService.DeleteDiscount(deleteDiscountParamDto);
        return ApiResponse.onSuccess(DiscountConverter.toDeleteDiscountResponseDto());
    }

    @GetMapping("/{storeId}/discounts")
    @Operation(summary = "사장님 과거 할인 목록 가져오기 API", description = "사장님 과거 할인 목록 가져오기 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "STORE404", description = "해당 가게가 존재하지 않습니다.")

    })
    @Parameters({
            @Parameter(name = "storeId", description = "가게 식별자, PathVariable")
    })
    public ApiResponse<DiscountResponseDto.GetDiscountedListResponseDto> GetDiscountedList(@PathVariable("storeId") Long storeId) {

        DiscountParameterDto.GetDiscountedListParamDto getDiscountedListParamDto = DiscountDtoConverter.INSTANCE.toGetDiscountedListParamDto(storeId);
        List<Discount> discountedList = discountService.GetDiscountedList(getDiscountedListParamDto);
        return ApiResponse.onSuccess(DiscountConverter.toGetDiscountedListResponseDto(discountedList));
    }
}
