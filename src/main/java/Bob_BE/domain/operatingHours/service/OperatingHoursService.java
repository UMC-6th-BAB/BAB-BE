package Bob_BE.domain.operatingHours.service;

import Bob_BE.domain.operatingHours.converter.OperatingHoursConverter;
import Bob_BE.domain.operatingHours.dto.request.OHRequestDto;
import Bob_BE.domain.operatingHours.dto.response.OHResponseDto;
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

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OperatingHoursService {

    private final OperatingHoursRepository operatingHoursRepository;
    private final StoreRepository storeRepository;


    @Transactional
    public OHResponseDto.OHCreateResultDto createOperatingHours(Long storeId, List<OHRequestDto.OHCreateRequestDto> requestDto){
        Store findStore = storeRepository.findById(storeId).orElseThrow(()->new StoreHandler(ErrorStatus.STORE_NOT_FOUND));

        log.info("requestDto: {}", requestDto);
        log.info("requestDtoSize: {}", requestDto.size());

        requestDto.forEach(request -> operatingHoursRepository.save(OperatingHoursConverter.toOperatingHours(findStore, request)));

        return new OHResponseDto.OHCreateResultDto(findStore.getId());
    }

}
