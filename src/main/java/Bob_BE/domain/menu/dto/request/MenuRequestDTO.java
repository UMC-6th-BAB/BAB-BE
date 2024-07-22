package Bob_BE.domain.menu.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Getter
public class MenuRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuCreateRequestDTO {
        private List<CreateMenuDTO> menus;

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class CreateMenuDTO {
            private String name;
            private Integer price;
            private String menuUrl;
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuUpdateRequestDTO {
        private String menuName;
        private Integer price;
        private String menuUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuDeleteRequestDTO {
        private List<Long> menuIds;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuImageUploadRequestDTO {
        private Long menuId;
        private MultipartFile imageFile;
    }
}
