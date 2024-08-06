package Bob_BE.domain.store.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StoreRequestDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class StoreCreateRequestDto{
        private String name;
        private Double longitude;
        private Double latitude;
        private String address;
        private String streetAddress;
        private String storeLink;
        private String registration;
        private String university;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class StoreUpdateRequestDto{
        private String name;
        private Double longitude;
        private Double latitude;
        private String address;
        private String streetAddress;
        private String storeLink;
        private String registration;
        private String university;
    }
}
