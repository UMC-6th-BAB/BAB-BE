package Bob_BE.domain.store.controller;

import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuCreateRequestDTO;
import Bob_BE.domain.menu.dto.response.MenuResponseDTO.CreateMenuResponseDTO;
import Bob_BE.domain.store.service.StoreService;
import Bob_BE.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<CreateMenuResponseDTO>>> createMenus(
            @PathVariable Long storeId,
            @RequestBody MenuCreateRequestDTO requestDTO
    ){
        var response = storeService.createMenus(storeId, requestDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
