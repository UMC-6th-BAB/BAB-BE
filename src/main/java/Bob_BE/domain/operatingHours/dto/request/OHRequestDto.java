package Bob_BE.domain.operatingHours.dto.request;

import Bob_BE.domain.operatingHours.entity.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

public class OHRequestDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class OHCreateRequestDto {
        private DayOfWeek day;
        private Time openTime;
        private Time closeTime;
        private Time breakStartTime;
        private Time breakEndTime;
        private boolean holiday;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class OHUpdateRequestDto{
        private DayOfWeek day;
        private Time openTime;
        private Time closeTime;
        private Time breakStartTime;
        private Time breakEndTime;

    }
}
