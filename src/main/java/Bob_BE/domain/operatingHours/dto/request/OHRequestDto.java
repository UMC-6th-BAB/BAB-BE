package Bob_BE.domain.operatingHours.dto.request;

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
public class OHRequestDto {

    private DayOfWeek day;
    private Time openTime;
    private Time closeTime;
    private BreakTime breakTime;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class BreakTime{
        private Time startTime;
        private Time endTime;
    }

}
