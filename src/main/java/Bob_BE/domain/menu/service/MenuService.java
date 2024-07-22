package Bob_BE.domain.menu.service;

import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuCreateRequestDTO;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuDeleteRequestDTO;
import Bob_BE.domain.menu.dto.request.MenuRequestDTO.MenuUpdateRequestDTO;
import Bob_BE.domain.menu.dto.response.MenuResponseDTO;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.menu.repository.MenuRepository;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.global.response.ApiResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    MenuResponseDTO.Result responseResult = null;


    public ApiResponse<?> createMenus(Long storeId, MenuCreateRequestDTO requestDTO) {
        List<MenuCreateRequestDTO.CreateMenuDTO> menus = requestDTO.getMenus();
        for (MenuCreateRequestDTO.CreateMenuDTO menu : menus) {
            Menu newMenu = Menu.builder()
                    .menuName(menu.getName())
                    .price(menu.getPrice())
                    .menuUrl(menu.getMenuUrl())
                    .store(Store.builder().id(storeId).build())
                    .build();
            newMenu = menuRepository.save(newMenu);
            responseResult = MenuResponseDTO.Result.builder()
                    .id(newMenu.getId())
                    .menuName(newMenu.getMenuName())
                    .price(newMenu.getPrice())
                    .menuUrl(newMenu.getMenuUrl())
                    .store(MenuResponseDTO.Result.StoreDTO.builder()
                            .id(newMenu.getStore().getId())
                            .name(newMenu.getStore().getName())  // 필요시 사용 또는 확장
                            .build())
                    .build();
        }
        return ApiResponse.onSuccess(responseResult);
    }

    public ApiResponse<?> updateMenu(Long menuId, MenuUpdateRequestDTO requestDTO){
        Optional<Menu> menuOptional = menuRepository.findById(menuId);
        if (menuOptional.isPresent()){
            Menu menu = menuOptional.get();
            menu.setMenuName(requestDTO.getMenuName());
            menu.setPrice(requestDTO.getPrice());
            menu.setMenuUrl(requestDTO.getMenuUrl());
            menuRepository.save(menu);
            return ApiResponse.onSuccess(
                    new MenuResponseDTO.Result(
                            menu.getId(),
                            menu.getMenuName(),
                            menu.getPrice(),
                            menu.getMenuUrl(),
                            null
                    )
            );
        }
        return ApiResponse.onFailure("COMMON404", "메뉴를 찾을 수 없습니다.", null);
    }

    public ApiResponse<?> deleteMenus(MenuDeleteRequestDTO requestDTO){
        List<Long> menuIds = requestDTO.getMenuIds();
        for (Long menuId: menuIds){
            Optional<Menu> menuOptional = menuRepository.findById(menuId);
            if (menuOptional.isPresent()){
                menuRepository.deleteById(menuId);
            }else{
                return ApiResponse.onFailure("COMMON404", "메뉴를 찾을 수 없습니다.", null);
            }
        }
        return ApiResponse.onSuccess(null);
    }

    public ApiResponse<?> uploadMenuImage(Long menuId, MultipartFile imageFile){
        Optional<Menu> menuOptional = menuRepository.findById(menuId);
        if (menuOptional.isPresent()){
            Menu menu = menuOptional.get();
            String imageUrl = saveImageFile(imageFile);
            menu.setMenuUrl(imageUrl);
            menuRepository.save(menu);
            return ApiResponse.onSuccess(
                    new MenuResponseDTO.Result(
                            menu.getId(),
                            menu.getMenuName(),
                            menu.getPrice(),
                            menu.getMenuUrl(),
                            null
                    )
            );
        }
        return ApiResponse.onFailure("COMMON404", "메뉴를 찾을 수 없습니다.", null);
    }

    private String saveImageFile(MultipartFile imageFile){
        // TODO:파일 저장 관련 로직 구현
        return "http://example.com/image";
    }
}
