package Bob_BE.domain.menu.controller;

import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuCreateRequestDTO;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuDeleteRequestDTO;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuUpdateRequestDTO;
import Bob_BE.domain.menu.dto.response.MenuResponseDTO;
import Bob_BE.domain.menu.service.MenuService;
import Bob_BE.domain.store.service.StoreService;
import Bob_BE.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;
    private final StoreService storeService;

    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<ApiResponse<?>> createMenus(
            @PathVariable Long storeId,
            @RequestBody MenuCreateRequestDTO requestDTO
    ){
        ApiResponse<?> response = storeService.createMenus(storeId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{menuId}")
    public ResponseEntity<ApiResponse<?>> updateMenu(
            @PathVariable Long menuId,
            @RequestBody MenuUpdateRequestDTO requestDTO
    ){
        MenuResponseDTO.CreateMenuResponseDTO response = menuService.updateMenu(menuId, requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> deleteMenus(
            @RequestBody MenuDeleteRequestDTO requestDTO
    ){
        ApiResponse<?> response = menuService.deleteMenus(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{menuId}/upload-image")
    public ResponseEntity<ApiResponse<?>> uploadMenuImage(
            @PathVariable Long menuId,
            @RequestParam("imageFile") MultipartFile imageFile
    ){
        MenuResponseDTO.CreateMenuResponseDTO response = menuService.uploadMenuImage(menuId, imageFile);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
