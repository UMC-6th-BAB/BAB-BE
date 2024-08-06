
package Bob_BE.domain.menu.dto.response;

import Bob_BE.domain.store.dto.response.StoreResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class MenuResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMenuResponseDto {
        private Long id;
        private String menuName;
        private Integer price;
        private String menuImageUrl;
        private StoreResponseDto.MenuCreateResultDto store;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeleteMenuResponseDto {
        private Long id;
        private String message;
    }
}
