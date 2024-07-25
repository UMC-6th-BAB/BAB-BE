package Bob_BE.domain.menu.controller;

import Bob_BE.domain.menu.dto.request.MenuRequestDto.MenuDeleteRequestDto;
import Bob_BE.domain.menu.dto.request.MenuRequestDto.MenuUpdateRequestDto;
import Bob_BE.domain.menu.dto.response.MenuResponseDto;
import Bob_BE.domain.menu.dto.response.MenuResponseDto.DeleteMenuResponseDto;
import Bob_BE.domain.menu.service.MenuService;
import Bob_BE.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PatchMapping("/{menuId}")
    public ApiResponse<MenuResponseDto.CreateMenuResponseDto> updateMenu(
            @PathVariable Long menuId,
            @RequestBody MenuUpdateRequestDto requestDto
    ){
        MenuResponseDto.CreateMenuResponseDto response = menuService.updateMenu(menuId, requestDto);
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping
    public ApiResponse<List<DeleteMenuResponseDto>> deleteMenus(
            @RequestBody MenuDeleteRequestDto requestDto
    ){
        List<DeleteMenuResponseDto> response = menuService.deleteMenus(requestDto);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/{menuId}/upload-image")
    public ApiResponse<MenuResponseDto.CreateMenuResponseDto> uploadMenuImage(
            @PathVariable Long menuId,
            @RequestParam("imageFile") MultipartFile imageFile
    ){
        MenuResponseDto.CreateMenuResponseDto response = menuService.uploadMenuImage(menuId, imageFile);
        return ApiResponse.onSuccess(response);
    }
}
