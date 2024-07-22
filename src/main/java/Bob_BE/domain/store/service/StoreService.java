package Bob_BE.domain.store.service;

import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuCreateRequestDTO;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuCreateRequestDTO.CreateMenuDTO;
import Bob_BE.domain.menu.dto.response.MenuResponseDTO;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.menu.repository.MenuRepository;
import Bob_BE.domain.store.dto.response.StoreResponseDTO.MenuCreateResultDTO;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.repository.StoreRepository;
import Bob_BE.global.response.ApiResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    public ApiResponse<?> createMenus(Long storeId, MenuCreateRequestDTO requestDTO) {
        List<CreateMenuDTO> menus = requestDTO.getMenus();
        Optional<Store> storeOptional = storeRepository.findById(storeId);
        Store store = storeOptional.get();

        MenuResponseDTO.CreateMenuResponseDTO responseResult = null;
        for (MenuCreateRequestDTO.CreateMenuDTO menu : menus) {
            Menu newMenu = Menu.builder()
                    .menuName(menu.getName())
                    .price(menu.getPrice())
                    .menuUrl(menu.getMenuUrl())
                    .store(store)
                    .build();
            newMenu = menuRepository.save(newMenu);
            responseResult = MenuResponseDTO.CreateMenuResponseDTO.builder()
                    .id(newMenu.getId())
                    .menuName(newMenu.getMenuName())
                    .price(newMenu.getPrice())
                    .menuUrl(newMenu.getMenuUrl())
                    .store(MenuCreateResultDTO.builder()
                            .id(store.getId())
                            .name(store.getName())
                            .build())
                    .build();
        }
        return ApiResponse.onSuccess(responseResult);
    }
}