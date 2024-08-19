package Bob_BE.domain.menu.converter;

import Bob_BE.domain.menu.dto.response.MenuResponseDto;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.signatureMenu.entity.SignatureMenu;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class MenuConverter {

    // Menu Create
    public static MenuResponseDto.CreateMenuResponseDto toCreateMenuRegisterResponseDto(Menu menu, boolean isSignature) {
        return MenuResponseDto.CreateMenuResponseDto.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuImageUrl(menu.getMenuUrl())
                .store(StoreResponseDto.MenuCreateResultDto.builder()
                        .id(menu.getStore().getId())
                        .name(menu.getStore().getName())
                        .build())
                .isSignature(isSignature)
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

    public static List<MenuResponseDto.CreateMenuResponseDto> toStoreMenuDtoList(List<Menu> menuList, SignatureMenu signatureMenu){
        return menuList.stream()
                .map(menu -> toStoreMenuDto(menu, signatureMenu))
                .collect(Collectors.toList());
    }

    public static MenuResponseDto.CreateMenuResponseDto toStoreMenuDto(Menu menu, SignatureMenu signatureMenu){
        Menu sigMenu = signatureMenu.getMenu();
        Boolean isSignature = sigMenu.equals(menu);
        String menuImageUrl = null;

        if(menu.getMenuUrl() != null){
            menuImageUrl = menu.getMenuUrl();
        }

        return MenuResponseDto.CreateMenuResponseDto.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuImageUrl(menuImageUrl)
                .store(StoreResponseDto.MenuCreateResultDto.builder()
                        .id(menu.getStore().getId())
                        .name(menu.getStore().getName())
                        .build())
                .isSignature(isSignature)
                .build();
    }
}