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
import Bob_BE.domain.signatureMenu.entity.SignatureMenu;
import Bob_BE.domain.signatureMenu.repository.SignatureMenuRepository;
import Bob_BE.domain.store.converter.StoreConverter;
import Bob_BE.domain.store.dto.request.StoreRequestDto;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.dto.parameter.StoreParameterDto;
import Bob_BE.domain.store.dto.response.StoreResponseDto.GetStoreSearchDto;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.repository.StoreRepository;
import Bob_BE.domain.storeUniversity.entity.StoreUniversity;
import Bob_BE.domain.storeUniversity.repository.StoreUniversityRepository;
import Bob_BE.domain.student.entity.Student;
import Bob_BE.domain.university.entity.University;
import Bob_BE.domain.university.repository.UniversityRepository;
import Bob_BE.domain.storeUniversity.service.StoreUniversityService;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.exception.handler.ImageHandler;
import Bob_BE.global.response.exception.handler.MenuHandler;

import Bob_BE.global.util.aws.S3StorageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


import java.util.Map;

import Bob_BE.global.response.exception.handler.OwnerHandler;

import Bob_BE.global.response.exception.handler.UniversityHandler;
import Bob_BE.global.util.google.GoogleCloudOCRService;
import jakarta.validation.Valid;
import Bob_BE.global.response.exception.handler.StoreHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final SignatureMenuRepository signatureMenuRepository;

    private final GoogleCloudOCRService googleCloudOCRService;

    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;



    @Transactional
    public List<MenuResponseDto.CreateMenuResponseDto> createMenus(Long storeId, MenuCreateRequestDto requestDto) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new MenuHandler(ErrorStatus.STORE_NOT_FOUND));
        List<CreateMenuDto> menus = requestDto.getMenus();

        long signatureMenuSize = menus.stream()
                .filter(MenuCreateRequestDto.CreateMenuDto::getIsSignature).count();

        if (signatureMenuSize == 0 || signatureMenuSize > 1){
            throw new MenuHandler(ErrorStatus.INVALID_SIGNATURE_MENU_COUNT);
        }

        return menus.stream().map(menu -> {
            Menu newMenu = Menu.builder()
                    .menuName(menu.getName())
                    .price(menu.getPrice())
                    .menuUrl(menu.getMenuUrl())
                    .store(store)
                    .build();
            newMenu = menuRepository.save(newMenu);

            if (menu.getIsSignature()){
                SignatureMenu signatureMenu = SignatureMenu.builder()
                        .menu(newMenu)
                        .store(store)
                        .build();
                signatureMenuRepository.save(signatureMenu);
            }
            return MenuConverter.toCreateMenuRegisterResponseDto(newMenu, menu.getIsSignature());
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
                }).toList();
    }
  
    /**
     * 가게 상세 페이지 API : 가게 데이터 가져오기
     * return : Store
     */
    public Store GetStoreData(StoreParameterDto.GetStoreDataParamDto param) {

        return storeRepository.findById(param.getStoreId())
                .orElseThrow(() -> new StoreHandler(ErrorStatus.STORE_NOT_FOUND));
    }
    public StoreResponseDto.CertificateResultDto registerCertificates(MultipartFile file) throws IOException {

        List<Map.Entry<String, Integer>> texts = googleCloudOCRService.detectTextGcs(file);

        String data = "";

        List<String> datas = new ArrayList<>();

        int referenceY = 0;

        for (Map.Entry<String, Integer> text: texts){

            if (text.getKey().equals(":")) {
                datas.add(data.trim());
                data = "";
                referenceY = text.getValue();
            } else if (text.getValue() >= referenceY - 10 && text.getValue() <= referenceY + 10){
                data = new StringBuilder(data).append(text.getKey()).append(" ").toString();
                log.info("data: {}", data);
            }
        }

        datas.add(data);

        return StoreConverter.toCertificateResultDto(datas);
    }

    @Cacheable(value = "storeSearch", key = "#param.keyword")
    public List<StoreResponseDto.GetStoreSearchDto> searchStoreWithMenus(
            StoreParameterDto.GetSearchKeywordParamDto param,
            Student student
    ) {
        String keyword = param.getKeyword();
        Long universityId = student.getUniversity().getId();

        List<Store> stores = storeRepository.findStoresByMenuKeyword(keyword, universityId);

        String geoKey = "locations";
        String universityIdentifier = "university:" + universityId;

        return stores.stream()
                .map(store -> {
                    String storeIdentifier = "store:" + store.getId();
                    Distance distance = redisTemplate.opsForGeo()
                            .distance(geoKey, universityIdentifier, storeIdentifier, Metrics.KILOMETERS);

                    return StoreConverter.toStoreSearchResponseDto(
                            store,
                            keyword,
                            distance != null ? distance.getValue() : null
                    );
                })
                .filter(dto -> !dto.getMenuList().isEmpty())
                .sorted(Comparator.comparing(StoreResponseDto.GetStoreSearchDto::getDistanceFromUniversityKm))
                .toList();
    }

    public void saveAllStoreLocationsToRedis(){
        List<Store> stores = storeRepository.findAll();
        String key = "locations";

        for (Store store : stores){
            redisTemplate.opsForGeo().add(key, new Point(store.getLongitude(), store.getLatitude()), "store:"+store.getId());
        }
    }
}