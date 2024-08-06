package Bob_BE.domain.storeUniversity.service;

import Bob_BE.domain.store.dto.request.StoreRequestDto;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.storeUniversity.entity.StoreUniversity;
import Bob_BE.domain.storeUniversity.repository.StoreUniversityRepository;
import Bob_BE.domain.university.entity.University;
import Bob_BE.domain.university.repository.UniversityRepository;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.exception.handler.StoreHandler;
import Bob_BE.global.response.exception.handler.UniversityHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreUniversityService {
    private final StoreUniversityRepository storeUniversityRepository;
    private final UniversityRepository universityRepository;

    @Transactional
    public void saveStoreUniversity(Store store, String universityName){
        University university = universityRepository.findByUniversityName(universityName).orElseThrow(()->new UniversityHandler(ErrorStatus.UNIVERSITY_NOT_FOUND));

        StoreUniversity storeUniversity = StoreUniversity.builder()
                .university(university)
                .store(store)
                .build();

        storeUniversityRepository.save(storeUniversity);
    }

    @Transactional
    public void updateStoreUniversity(Long storeId, StoreRequestDto.StoreUpdateRequestDto requestDto){

        StoreUniversity findStoreUniversity = storeUniversityRepository.findByStoreId(storeId).orElseThrow(()-> new UniversityHandler(ErrorStatus.UNIVERSITY_NOT_FOUND));

        University university = universityRepository.findByUniversityName(requestDto.getUniversity()).orElseThrow(()->new UniversityHandler(ErrorStatus.UNIVERSITY_NOT_FOUND));

        findStoreUniversity.updateStoreUniversity(university);

        storeUniversityRepository.save(findStoreUniversity);
    }

}
