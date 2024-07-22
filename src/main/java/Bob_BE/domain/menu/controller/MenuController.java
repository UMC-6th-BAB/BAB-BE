package Bob_BE.domain.menu.controller;

import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuCreateRequestDTO;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuDeleteRequestDTO;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuUpdateRequestDTO;
import Bob_BE.domain.menu.service.MenuService;
import Bob_BE.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/menus") // 클래스 레벨에서 RequestMapping 사용
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/stores/{storeId}/menus") // StoreController로 이동 필요
    public ResponseEntity<ApiResponse<?>> createMenus(
            @PathVariable Long storeId,
            @RequestBody MenuCreateRequestDTO requestDTO
    ){
        ApiResponse<?> response = menuService.createMenus(storeId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{menuId}") // 중복된 경로 수정
    public ResponseEntity<ApiResponse<?>> updateMenu(
            @PathVariable Long menuId,
            @RequestBody MenuUpdateRequestDTO requestDTO
    ){
        ApiResponse<?> response = menuService.updateMenu(menuId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping // 중복된 경로 수정
    public ResponseEntity<ApiResponse<?>> deleteMenus(
            @RequestBody MenuDeleteRequestDTO requestDTO
    ){
        ApiResponse<?> response = menuService.deleteMenus(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{menuId}/upload-image") // 중복된 경로 수정
    public ResponseEntity<ApiResponse<?>> uploadMenuImage(
            @PathVariable Long menuId,
            @RequestParam("imageFile") MultipartFile imageFile
    ){
        ApiResponse<?> response = menuService.uploadMenuImage(menuId, imageFile);
        return ResponseEntity.ok(response);
    }
}
