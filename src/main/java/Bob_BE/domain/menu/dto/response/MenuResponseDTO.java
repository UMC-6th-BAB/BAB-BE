
package Bob_BE.domain.menu.dto.response;

import Bob_BE.domain.store.dto.response.StoreResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class MenuResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMenuResponseDTO {
        private Long id;
        private String menuName;
        private Integer price;
        private String menuUrl;
        private StoreResponseDTO.MenuCreateResultDTO store;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeleteMenuResponseDTO {
        private Long id;
        private String message;
    }
}
