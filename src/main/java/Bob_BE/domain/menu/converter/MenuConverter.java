package Bob_BE.domain.menu.converter;

import Bob_BE.domain.menu.dto.response.MenuResponseDto;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MenuConverter {

    // Menu Create
    public static MenuResponseDto.CreateMenuResponseDto toCreateMenuRegisterResponseDto(Menu menu) {
        return MenuResponseDto.CreateMenuResponseDto.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuImageUrl(menu.getMenuUrl())
                .store(StoreResponseDto.MenuCreateResultDto.builder()
                        .id(menu.getStore().getId())
                        .name(menu.getStore().getName())
                        .build())
                .build();
    }

    // Menu Update, Menu Image Update
    public static MenuResponseDto.CreateMenuResponseDto toCreateMenuResponseDto(Menu menu) {
        return MenuResponseDto.CreateMenuResponseDto.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuImageUrl(menu.getMenuUrl())
                .build();
    }

    //Menu Delete
    public static MenuResponseDto.DeleteMenuResponseDto toDeleteMenuResponseDto(Long menuId) {
        return MenuResponseDto.DeleteMenuResponseDto.builder()
                .id(menuId)
                .message("삭제되었습니다.")
                .build();
    }
}