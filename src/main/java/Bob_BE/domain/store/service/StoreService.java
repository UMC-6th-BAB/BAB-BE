package Bob_BE.domain.store.service;

import Bob_BE.domain.banner.entity.Banner;
import Bob_BE.domain.banner.repository.BannerRepository;
import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.discountMenu.entity.DiscountMenu;
import Bob_BE.domain.discountMenu.repository.DiscountMenuRepository;
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
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.repository.StoreRepository;
import Bob_BE.domain.storeUniversity.entity.StoreUniversity;
import Bob_BE.domain.storeUniversity.repository.StoreUniversityRepository;
import Bob_BE.domain.university.entity.University;
import Bob_BE.domain.university.repository.UniversityRepository;
import Bob_BE.domain.storeUniversity.service.StoreUniversityService;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.exception.handler.ImageHandler;
import Bob_BE.global.response.exception.handler.MenuHandler;

import Bob_BE.global.util.aws.S3StorageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import Bob_BE.global.response.exception.handler.OwnerHandler;

import Bob_BE.global.response.exception.handler.UniversityHandler;
import jakarta.validation.Valid;
import Bob_BE.global.response.exception.handler.StoreHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final OwnerRepository ownerRepository;
    private final UniversityRepository universityRepository;
    private final StoreUniversityRepository storeUniversityRepository;
    private final DiscountMenuRepository discountMenuRepository;

    private final StoreUniversityService storeUniversityService;
    private final S3StorageService s3StorageService;
    private final BannerRepository bannerRepository;


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
    public StoreResponseDto.StoreCreateResultDto createStore(Long ownerId, StoreRequestDto.StoreCreateRequestDto requestDto, MultipartFile[] bannerFiles){
        Owner findOwner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerHandler(ErrorStatus.OWNER_NOT_FOUND));

        Store newStore = StoreConverter.toStore(findOwner, requestDto);
        storeRepository.save(newStore);

        storeUniversityService.saveStoreUniversity(newStore, requestDto.getUniversity());

        List<Banner> banners = new ArrayList<>();
        for (MultipartFile bannerFile : bannerFiles){
            if (bannerFile != null && !bannerFile.isEmpty()){
                try{
                    String bannerUrl = s3StorageService.uploadFile(bannerFile, "Banners");
                    Banner banner = Banner.builder()
                            .bannerName(bannerFile.getOriginalFilename())
                            .bannerType(bannerFile.getContentType())
                            .bannerUrl(bannerUrl)
                            .store(newStore)
                            .build();

                    banners.add(banner);
                    bannerRepository.save(banner);
                }catch (IOException e){
                    throw new ImageHandler(ErrorStatus.FILE_UPLOAD_FAILED);
                }
            }
        }

        newStore.setBannerList(banners);
        return StoreConverter.toCreateStoreResponseDto(newStore);
    }

    @Transactional
    public StoreResponseDto.StoreUpdateResultDto updateStore(Long storeId, StoreRequestDto.StoreUpdateRequestDto requestDto){
        Store findStore = storeRepository.findById(storeId).orElseThrow(()-> new StoreHandler(ErrorStatus.STORE_NOT_FOUND));

        findStore.updateStore(requestDto);

        storeUniversityService.updateStoreUniversity(storeId, requestDto);

        storeRepository.save(findStore);

        return new StoreResponseDto.StoreUpdateResultDto(findStore.getId(), findStore.getName());
    }

    @Transactional
    public StoreResponseDto.StoreDeleteResultDto deleteStore(Long storeId){

        Store findStore = storeRepository.findById(storeId).orElseThrow(()-> new StoreHandler(ErrorStatus.STORE_NOT_FOUND));

        storeRepository.delete(findStore);

        return StoreConverter.toDeleteStoreResponseDto(findStore);
    }

    /**
     * 오늘의 할인 가게 리스트 가져오기 API
     * return : List<StoreResponseDto.GetOnSaleStoreDataDto>
     */
    public List<StoreResponseDto.GetOnSaleStoreDataDto> GetOnSaleStoreListData(@Valid StoreParameterDto.GetOnSaleStoreListParamDto param) {

        University findUniversity = universityRepository.findById(param.getUniversityId())
                .orElseThrow(() -> new UniversityHandler(ErrorStatus.UNIVERSITY_NOT_FOUND));

        List<StoreResponseDto.StoreAndDiscountDataDto> storeAndDiscountDataDtoList = storeRepository.GetOnSaleStoreAndDiscount(findUniversity);

        List<StoreResponseDto.GetOnSaleStoreDataDto> getOnSaleStoreDataDtoList = StoreConverter.toGetOnSaleStoreDataDtoList(storeAndDiscountDataDtoList);

        getOnSaleStoreDataDtoList = storeRepository.GetOnSaleMenuData(getOnSaleStoreDataDtoList);

        return getOnSaleStoreDataDtoList;
    }

    /**
     * 지도 핑을 위한 데이터 가져오기 API
     * return : List<StoreDataDto>
     */
    public List<StoreResponseDto.StoreDataDto> GetStoreDataList(StoreParameterDto.GetDataForPingParamDto param) {

        University findUniversity = universityRepository.findById(param.getUniversityId())
                .orElseThrow(() -> new UniversityHandler(ErrorStatus.UNIVERSITY_NOT_FOUND));

        List<StoreUniversity> storeUniversityList = storeUniversityRepository.findAllByUniversity(findUniversity)
                .orElse(new ArrayList<>());

        List<Store> storeList = storeUniversityList.stream()
                .map(StoreUniversity::getStore)
                .collect(Collectors.toList());

        return storeList.stream()
                .map(store -> {
                    if(store.getDiscountList().stream().anyMatch(Discount::getInProgress)) {
                        AtomicInteger discountPrice = new AtomicInteger();

                        store.getSignatureMenu().getMenu().getDiscountMenuList()
                                .forEach(discountMenu -> {

                                    if(discountMenu.getDiscount().getInProgress())
                                        discountPrice.addAndGet(discountMenu.getDiscountPrice());
                                });

                        if (discountPrice.get() == 0) {
                            DiscountMenu discountMenu = discountMenuRepository.GetDiscountMenuByStoreAndMaxDiscountPrice(store);

                            return StoreConverter.toStoreDataDto(store, discountMenu.getMenu(), discountMenu.getDiscountPrice());
                        }

                        return StoreConverter.toStoreDataDto(store, store.getSignatureMenu().getMenu(),discountPrice.get());
                    }
                    else {
                        return StoreConverter.toStoreDataDto(store, store.getSignatureMenu().getMenu(), 0);
                    }
                }).collect(Collectors.toList());
    }
  
    /**
     * 가게 상세 페이지 API : 가게 데이터 가져오기
     * return : Store
     */
    public Store GetStoreData(StoreParameterDto.GetStoreDataParamDto param) {

        return storeRepository.findById(param.getStoreId())
                .orElseThrow(() -> new StoreHandler(ErrorStatus.STORE_NOT_FOUND));
    }

}
