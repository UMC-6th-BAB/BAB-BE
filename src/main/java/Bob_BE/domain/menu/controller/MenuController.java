package Bob_BE.domain.menu.controller;

import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuCreateRequestDTO;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuDeleteRequestDTO;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuUpdateRequestDTO;
import Bob_BE.domain.menu.service.MenuService;
import Bob_BE.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor // @RequiredArgsConstructor 어노테이션을 추가하여 final 필드인 menuService를 생성자 주입으로 초기화합니다.
public class MenuController {
    private final MenuService menuService; // final 필드로 선언된 menuService는 생성자에서 초기화됩니다.

    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<ApiResponse<?>> createMenus(
            @PathVariable Long storeId,
            @RequestBody MenuCreateRequestDTO requestDTO
    ){
        ApiResponse<?> response = menuService.createMenus(storeId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/menus/{menuId}")
    public ResponseEntity<ApiResponse<?>> updateMenu(
            @PathVariable Long menuId,
            @RequestBody MenuUpdateRequestDTO requestDTO
    ){
        ApiResponse<?> response = menuService.updateMenu(menuId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/menus")
    public ResponseEntity<ApiResponse<?>> deleteMenus(
            @RequestBody MenuDeleteRequestDTO requestDTO
    ){
        ApiResponse<?> response = menuService.deleteMenus(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/menus/{menuId}/upload-image")
    public ResponseEntity<ApiResponse<?>> uploadMenuImage(
            @PathVariable Long menuId,
            @RequestParam("imageFile") MultipartFile imageFile
    ){
        ApiResponse<?> response = menuService.uploadMenuImage(menuId, imageFile);
        return ResponseEntity.ok(response);
    }
}
