
package Bob_BE.domain.menu.dto.response;

import Bob_BE.domain.menu.dto.response.MenuResponseDTO.Result.StoreDTO;
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

//    // 메뉴 사진 등록을 위한 추가 생성자
//    public MenuResponseDTO(boolean isSuccess, String code, String message, Long menuId, String menuUrl) {
//        this.isSuccess = isSuccess;
//        this.code = code;
//        this.message = message;
//        this.result = new Result(menuId, null, null, menuUrl, null);
//    }
//
//    // 메뉴 삭제 응답을 위한 추가 생성자
//    public MenuResponseDTO(boolean isSuccess, String code, String message) {
//        this.isSuccess = isSuccess;
//        this.code = code;
//        this.message = message;
//        this.result = null;
//    }
}
