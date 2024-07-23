package Bob_BE.domain.menu.converter;

import Bob_BE.domain.menu.dto.response.MenuResponseDTO;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.store.dto.response.StoreResponseDTO;

public class MenuConverter {

    private MenuConverter() {}

    // Menu Create
    public static MenuResponseDTO.CreateMenuResponseDTO toCreateMenuRegisterResponseDTO(Menu menu) {
        return MenuResponseDTO.CreateMenuResponseDTO.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuUrl(menu.getMenuUrl())
                .store(StoreResponseDTO.MenuCreateResultDTO.builder()
                        .id(menu.getStore().getId())
                        .name(menu.getStore().getName())
                        .build())
                .build();
    }

    // Menu Update, Menu Image Update
    public static MenuResponseDTO.CreateMenuResponseDTO toCreateMenuResponseDTO(Menu menu) {
        return MenuResponseDTO.CreateMenuResponseDTO.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuUrl(menu.getMenuUrl())
                .build();
    }

    //Menu Delete
    public static MenuResponseDTO.DeleteMenuResponseDTO toDeleteMenuResponseDTO(Long menuId) {
        return MenuResponseDTO.DeleteMenuResponseDTO.builder()
                .id(menuId)
                .message("삭제되었습니다.")
                .build();
    }
}