package Bob_BE.domain.operatingHours.dto.response;

import Bob_BE.domain.operatingHours.dto.request.OHRequestDto;
import Bob_BE.domain.operatingHours.entity.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OHResponseDto {

    private Long storeId;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class StoreOperatingHoursDto{
        private DayOfWeek day;
        private Time openTime;
        private Time closeTime;
        private OHResponseDto.BreakTimeDto breakTime;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class BreakTimeDto{
        private Time startTime;
        private Time endTime;
    }

}
