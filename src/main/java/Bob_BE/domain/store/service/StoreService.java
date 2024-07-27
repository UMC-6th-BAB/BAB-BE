package Bob_BE.domain.store.service;

import Bob_BE.domain.menu.converter.MenuConverter;
import Bob_BE.domain.menu.dto.request.MenuRequestDto.MenuCreateRequestDto;
import Bob_BE.domain.menu.dto.request.MenuRequestDto.MenuCreateRequestDto.CreateMenuDto;
import Bob_BE.domain.menu.dto.response.MenuResponseDto;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.menu.repository.MenuRepository;
import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.owner.repository.OwnerRepository;
import Bob_BE.domain.store.converter.StoreConverter;
import Bob_BE.domain.store.dto.request.StoreRequestDto;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.dto.parameter.StoreParameterDto;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.repository.StoreRepository;
import Bob_BE.domain.storeUniversity.entity.StoreUniversity;
import Bob_BE.domain.storeUniversity.repository.StoreUniversityRepository;
import Bob_BE.domain.university.entity.University;
import Bob_BE.domain.university.repository.UniversityRepository;
import Bob_BE.domain.storeUniversity.service.StoreUniversityService;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.exception.handler.MenuHandler;

import java.util.ArrayList;
import java.util.List;
import Bob_BE.global.response.exception.handler.OwnerHandler;
import java.util.stream.Collectors;

import Bob_BE.global.response.exception.handler.UniversityHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final UniversityRepository universityRepository;
    private final StoreUniversityRepository storeUniversityRepository;
    private final OwnerRepository ownerRepository;

    private final StoreUniversityService storeUniversityService;


    @Transactional
    public List<MenuResponseDto.CreateMenuResponseDto> createMenus(Long storeId, MenuCreateRequestDto requestDto) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new MenuHandler(ErrorStatus.STORE_NOT_FOUND));
        List<CreateMenuDto> menus = requestDto.getMenus();

        return menus.stream().map(menu -> {
            Menu newMenu = Menu.builder()
                    .menuName(menu.getName())
                    .price(menu.getPrice())
                    .menuUrl(menu.getMenuUrl())
                    .store(store)
                    .build();
            newMenu = menuRepository.save(newMenu);
            return MenuConverter.toCreateMenuRegisterResponseDto(newMenu);
        }).toList();
    }

    @Transactional
    public StoreResponseDto.StoreCreateResultDto createStore(Long ownerId, StoreRequestDto.StoreCreateRequestDto requestDto){
        Owner findOwner = ownerRepository.findById(ownerId).orElseThrow(() -> new OwnerHandler(ErrorStatus.OWNER_NOT_FOUND));

        Store newStore = StoreConverter.toStore(findOwner, requestDto);

        storeUniversityService.saveStoreUniversity(newStore, requestDto.getUniversity());
        storeRepository.save(newStore);

        return StoreConverter.toCreateStoreResponseDto(newStore);
    }

    /**
     * 오늘의 할인 가게 리스트 가져오기 API
     * return : List<StoreResponseDto.GetOnSaleStoreDataDto>
     */
    public List<StoreResponseDto.GetOnSaleStoreDataDto> GetOnSaleStoreListData(@Valid StoreParameterDto.GetOnSaleStoreListParamDto param) {

        University findUniversity = universityRepository.findById(param.getUniversityId())
                .orElseThrow(() -> new UniversityHandler(ErrorStatus.UNIVERSITY_NOT_FOUND));

        List<StoreUniversity> storeUniversityList = storeUniversityRepository.findAllByUniversity(findUniversity)
                .orElse(new ArrayList<>());

        List<Store> storeList = storeUniversityList.stream()
                .map(StoreUniversity::getStore).collect(Collectors.toList());

        List<StoreResponseDto.GetOnSaleStoreDataDto> getOnSaleStoreDataDtoList = storeRepository.GetOnSaleStoreAndMenuData(storeList);

        return getOnSaleStoreDataDtoList;
    }
}
