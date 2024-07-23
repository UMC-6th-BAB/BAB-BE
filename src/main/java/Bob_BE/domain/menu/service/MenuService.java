package Bob_BE.domain.menu.service;

import Bob_BE.domain.menu.converter.MenuConverter;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuDeleteRequestDTO;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuUpdateRequestDTO;
import Bob_BE.domain.menu.dto.response.MenuResponseDTO;
import Bob_BE.domain.menu.dto.response.MenuResponseDTO.DeleteMenuResponseDTO;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.menu.repository.MenuRepository;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.exception.handler.MenuHandler;
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
                .orElseThrow(() -> new MenuHandler(ErrorStatus.MENU_NOT_FOUND));
        menu.setMenuName(requestDTO.getMenuName());
        menu.setPrice(requestDTO.getPrice());
        menu.setMenuUrl(requestDTO.getMenuUrl());
        menuRepository.save(menu);

        return MenuConverter.toCreateMenuResponseDTO(menu);
    }

    public List<DeleteMenuResponseDTO> deleteMenus(MenuDeleteRequestDTO requestDTO) {
        List<Long> menuIds = requestDTO.getMenuIds();
        return menuIds.stream().map(menuId -> {
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new MenuHandler(ErrorStatus.MENU_NOT_FOUND));
            menuRepository.delete(menu);
            return MenuConverter.toDeleteMenuResponseDTO(menuId);
        }).toList();
    }

    public MenuResponseDTO.CreateMenuResponseDTO uploadMenuImage(Long menuId, MultipartFile imageFile) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuHandler(ErrorStatus.MENU_NOT_FOUND));

        String imageUrl = saveImageFile(imageFile);
        menu.setMenuUrl(imageUrl);
        menuRepository.save(menu);

        return MenuConverter.toCreateMenuResponseDTO(menu);
    }

    private String saveImageFile(MultipartFile imageFile) {
        // TODO: 파일 저장 관련 로직 구현
        return "http://example.com/image";
    }
}
