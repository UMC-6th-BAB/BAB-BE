package Bob_BE.domain.operatingHours.converter;

import Bob_BE.domain.operatingHours.dto.request.OHRequestDto;
import Bob_BE.domain.operatingHours.entity.DayOfWeek;
import Bob_BE.domain.operatingHours.entity.OperatingHours;
import Bob_BE.domain.store.entity.Store;

public class OperatingHoursConverter {

    public static OperatingHours toOperatingHours(Store store, OHRequestDto.OHCreateRequestDto requestDto){

        return OperatingHours.builder()
                .store(store)
                .day(requestDto.getDay())
                .openTime(requestDto.getOpenTime())
                .closeTime(requestDto.getCloseTime())
                .breakStartTime(requestDto.getBreakStartTime())
                .breakEndTime(requestDto.getBreakEndTime())
                .holiday(false)
                .build();
    }
}
