package Bob_BE.domain.operatingHours.service;

import Bob_BE.domain.operatingHours.converter.OperatingHoursConverter;
import Bob_BE.domain.operatingHours.dto.request.OHRequestDto;
import Bob_BE.domain.operatingHours.dto.response.OHResponseDto;
import Bob_BE.domain.operatingHours.entity.DayOfWeek;
import Bob_BE.domain.operatingHours.entity.OperatingHours;
import Bob_BE.domain.operatingHours.repository.OperatingHoursRepository;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.repository.StoreRepository;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.exception.handler.StoreHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OperatingHoursService {

    private final OperatingHoursRepository operatingHoursRepository;
    private final StoreRepository storeRepository;


    @Transactional
    public OHResponseDto createOperatingHours(Long storeId, List<OHRequestDto> requestDto){
        Store findStore = storeRepository.findById(storeId).orElseThrow(()->new StoreHandler(ErrorStatus.STORE_NOT_FOUND));

        requestDto.forEach(request -> operatingHoursRepository.save(OperatingHoursConverter.toOperatingHours(findStore, request)));

        List<DayOfWeek> holiday = checkHoliday(requestDto);

        holiday.forEach(hd -> operatingHoursRepository.save(OperatingHoursConverter.toHolidays(findStore, hd)));

        return new OHResponseDto(findStore.getId());
    }

    @Transactional
    public OHResponseDto updateOperatingHours(Long storeId, List<OHRequestDto> requestDto){
        Store findStore = storeRepository.findById(storeId).orElseThrow(()->new StoreHandler(ErrorStatus.STORE_NOT_FOUND));

        List<OperatingHours> operatingHours = operatingHoursRepository.findAllByStoreId(storeId).orElseThrow(()-> new StoreHandler(ErrorStatus.OPERATING_HOURS_NOT_FOUND));

        List<DayOfWeek> holiday = checkHoliday(requestDto);

        updateOperatingHours(requestDto, operatingHours);

        setHolidays(holiday, operatingHours);

        return new OHResponseDto(findStore.getId());
    }

    public List<DayOfWeek> checkHoliday(List<OHRequestDto> requestDto){
        List<DayOfWeek> weeks = requestDto.stream().map(OHRequestDto::getDay).toList();

        List<DayOfWeek> dayOfWeeks = List.of(DayOfWeek.values());

        List<DayOfWeek> holiday = new ArrayList<>();

        for (DayOfWeek day : dayOfWeeks){
            if (!weeks.contains(day)){
                holiday.add(day);
            }
        }

        return holiday;
    }

    public void updateOperatingHours(List<OHRequestDto> requestDto, List<OperatingHours> operatingHours){
        operatingHours.stream()
                .forEach(oh -> requestDto.stream()
                        .filter(dto -> oh.getDay().equals(dto.getDay()))
                        .findFirst()
                        .ifPresent(dto -> {
                            oh.updateOH(dto);
                            operatingHoursRepository.save(oh);
                        }));

    }

    public void setHolidays(List<DayOfWeek> holidays, List<OperatingHours> operatingHours){
        operatingHours.stream()
                .filter(oh -> holidays.contains(oh.getDay()))
                .forEach(oh -> {
                    oh.updateHoliday();
                    operatingHoursRepository.save(oh);
                });
    }
}
