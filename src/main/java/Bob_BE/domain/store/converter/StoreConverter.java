package Bob_BE.domain.store.converter;

import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.store.dto.response.StoreResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class StoreConverter {

    public static StoreResponseDto.GetMenuNameListResponseDto toGetMenuNameListResponseDto (List<Menu> menuList) {

        List<StoreResponseDto.MenuNameDataDto> menuNameDataDtoList = menuList.stream()
                .map(menu -> {
                    return StoreResponseDto.MenuNameDataDto.builder()
                            .menuId(menu.getId())
                            .name(menu.getMenuName())
                            .build();
                }).collect(Collectors.toList());

        return StoreResponseDto.GetMenuNameListResponseDto.builder()
                .menuNameDataDtoList(menuNameDataDtoList)
                .totalDataNum(menuNameDataDtoList.size())
                .build();
    }
}
