package Bob_BE.domain.menu.service;

import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuDeleteRequestDTO;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuUpdateRequestDTO;
import Bob_BE.domain.menu.dto.response.MenuResponseDTO;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.menu.repository.MenuRepository;
import Bob_BE.global.response.ApiResponse;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.exception.handler.UserHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuResponseDTO.CreateMenuResponseDTO updateMenu(Long menuId, MenuUpdateRequestDTO requestDTO) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MENU_NOT_FOUND));
        menu.setMenuName(requestDTO.getMenuName());
        menu.setPrice(requestDTO.getPrice());
        menu.setMenuUrl(requestDTO.getMenuUrl());
        menuRepository.save(menu);

        return MenuResponseDTO.CreateMenuResponseDTO.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuUrl(menu.getMenuUrl())
                .build();
    }

    public ApiResponse<?> deleteMenus(MenuDeleteRequestDTO requestDTO) {
        List<Long> menuIds = requestDTO.getMenuIds();
        menuIds.forEach(menuId -> {
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.MENU_NOT_FOUND));
            menuRepository.delete(menu);
        });

        return ApiResponse.onSuccess("메뉴가 성공적으로 삭제되었습니다.");
    }

    public MenuResponseDTO.CreateMenuResponseDTO uploadMenuImage(Long menuId, MultipartFile imageFile) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.MENU_NOT_FOUND));

        String imageUrl = saveImageFile(imageFile);
        menu.setMenuUrl(imageUrl);
        menuRepository.save(menu);

        return MenuResponseDTO.CreateMenuResponseDTO.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuUrl(menu.getMenuUrl())
                .build();
    }

    private String saveImageFile(MultipartFile imageFile) {
        // TODO: 파일 저장 관련 로직 구현
        return "http://example.com/image";
    }
}
