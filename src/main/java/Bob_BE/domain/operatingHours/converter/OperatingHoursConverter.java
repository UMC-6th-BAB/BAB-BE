package Bob_BE.domain.operatingHours.converter;

import Bob_BE.domain.operatingHours.dto.request.OHRequestDto;
import Bob_BE.domain.operatingHours.dto.response.OHResponseDto;
import Bob_BE.domain.operatingHours.entity.DayOfWeek;
import Bob_BE.domain.operatingHours.entity.OperatingHours;
import Bob_BE.domain.store.entity.Store;

import java.util.List;

public class OperatingHoursConverter {

    public static OperatingHours toOperatingHours(Store store, OHRequestDto requestDto){

        return OperatingHours.builder()
                .store(store)
                .day(requestDto.getDay())
                .openTime(requestDto.getOpenTime())
                .closeTime(requestDto.getCloseTime())
                .breakStartTime(requestDto.getBreakTime().getStartTime())
                .breakEndTime(requestDto.getBreakTime().getEndTime())
                .holiday(false)
                .build();
    }

    public static OperatingHours toHolidays(Store store, DayOfWeek day){
        return OperatingHours.builder()
                .store(store)
                .day(day)
                .openTime(null)
                .closeTime(null)
                .breakStartTime(null)
                .breakEndTime(null)
                .holiday(true)
                .build();
    }

    public static List<OHResponseDto.StoreOperatingHoursDto> toStoreOperatingHoursDtoList(List<OperatingHours> operatingHoursList) {
        return operatingHoursList.stream()
                .map(OperatingHoursConverter::toStoreOperatingHours)
                .toList();
    }

    public static OHResponseDto.StoreOperatingHoursDto toStoreOperatingHours(OperatingHours operatingHours){
        OHResponseDto.BreakTimeDto breakTime = OHResponseDto.BreakTimeDto.builder()
                .startTime(operatingHours.getBreakStartTime())
                .endTime(operatingHours.getBreakEndTime())
                .build();

        return OHResponseDto.StoreOperatingHoursDto.builder()
                .day(operatingHours.getDay())
                .openTime(operatingHours.getOpenTime())
                .closeTime(operatingHours.getCloseTime())
                .breakTime(breakTime)
                .build();
    }


}
