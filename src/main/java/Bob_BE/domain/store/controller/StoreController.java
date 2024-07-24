package Bob_BE.domain.store.controller;

import Bob_BE.domain.menu.dto.request.MenuRequestDto.MenuCreateRequestDto;
import Bob_BE.domain.menu.dto.response.MenuResponseDto.CreateMenuResponseDto;
import Bob_BE.domain.store.service.StoreService;
import Bob_BE.global.response.ApiResponse;
import java.util.List;
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
}
