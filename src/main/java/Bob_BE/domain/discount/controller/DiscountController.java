package Bob_BE.domain.discount.controller;

import Bob_BE.domain.discount.dto.request.DiscountRequestDto;
import Bob_BE.domain.discount.dto.response.DiscountResponseDto;
import Bob_BE.domain.discount.service.DiscountService;
import Bob_BE.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores")
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping("/{storeId}/menus/discounts")
    public ApiResponse<DiscountResponseDto.CreateDiscountResponseDto> CreateDiscount(@RequestBody @Valid DiscountRequestDto.CreateDiscountRequestDto request, @PathVariable("storeId") Long storeId) {

        return null;
    }
}
