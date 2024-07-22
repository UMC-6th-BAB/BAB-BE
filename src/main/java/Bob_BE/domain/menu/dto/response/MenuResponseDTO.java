
package Bob_BE.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponseDTO {
    private Long id;
    private String menuName;
    private Integer price;
    private String menuUrl;

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private Long id;
        private String menuName;
        private Integer price;
        private String menuUrl;
        private StoreDTO store;

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class StoreDTO {
            private Long id;
            private String name;
        }
    }
}
