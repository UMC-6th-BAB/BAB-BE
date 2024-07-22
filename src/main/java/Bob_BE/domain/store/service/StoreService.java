package Bob_BE.domain.store.service;

import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuCreateRequestDTO;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuCreateRequestDTO.CreateMenuDTO;
import Bob_BE.domain.menu.dto.response.MenuResponseDTO;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.menu.repository.MenuRepository;
import Bob_BE.domain.store.dto.response.StoreResponseDTO.MenuCreateResultDTO;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.repository.StoreRepository;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.exception.handler.UserHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    public List<MenuResponseDTO.CreateMenuResponseDTO> createMenus(Long storeId, MenuCreateRequestDTO requestDTO) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.STORE_NOT_FOUND));
        List<CreateMenuDTO> menus = requestDTO.getMenus();

        return menus.stream().map(menu -> {
            Menu newMenu = Menu.builder()
                    .menuName(menu.getName())
                    .price(menu.getPrice())
                    .menuUrl(menu.getMenuUrl())
                    .store(store)
                    .build();
            newMenu = menuRepository.save(newMenu);
            return MenuResponseDTO.CreateMenuResponseDTO.builder()
                    .id(newMenu.getId())
                    .menuName(newMenu.getMenuName())
                    .price(newMenu.getPrice())
                    .menuUrl(newMenu.getMenuUrl())
                    .store(MenuCreateResultDTO.builder()
                            .id(store.getId())
                            .name(store.getName())
                            .build())
                    .build();
        }).toList();
    }
}
