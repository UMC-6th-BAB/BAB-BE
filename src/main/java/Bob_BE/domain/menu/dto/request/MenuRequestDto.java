package Bob_BE.domain.menu.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Getter
public class MenuRequestDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuCreateRequestDto {
        private List<CreateMenuDto> menus;

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class CreateMenuDto {
            private String name;
            private Integer price;
            private String menuUrl;
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuUpdateRequestDto {
        private String menuName;
        private Integer price;
        private String menuUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuDeleteRequestDto {
        private List<Long> menuIds;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuImageUploadRequestDto {
        private Long menuId;
        private MultipartFile imageFile;
    }
}
