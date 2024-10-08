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
import Bob_BE.domain.operatingHours.entity.OperatingHours;
import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.owner.repository.OwnerRepository;
import Bob_BE.domain.owner.service.OwnerService;
import Bob_BE.domain.signatureMenu.entity.SignatureMenu;
import Bob_BE.domain.signatureMenu.repository.SignatureMenuRepository;
import Bob_BE.domain.store.converter.StoreConverter;
import Bob_BE.domain.store.dto.request.StoreRequestDto;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.dto.parameter.StoreParameterDto;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.repository.StoreRepository;
import Bob_BE.domain.storeUniversity.entity.StoreUniversity;
import Bob_BE.domain.storeUniversity.repository.StoreUniversityRepository;
import Bob_BE.domain.student.entity.Student;
import Bob_BE.domain.student.repository.StudentRepository;
import Bob_BE.domain.student.service.StudentService;
import Bob_BE.domain.university.entity.University;
import Bob_BE.domain.university.repository.UniversityRepository;
import Bob_BE.domain.storeUniversity.service.StoreUniversityService;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.exception.GeneralException;
import Bob_BE.global.response.exception.handler.*;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import Bob_BE.global.util.JwtTokenProvider;
import Bob_BE.global.util.aws.S3StorageService;
import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


import java.util.Map;

import Bob_BE.global.util.google.GoogleCloudOCRService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final StoreUniversityRepository storeUniversityRepository;
    private final DiscountMenuRepository discountMenuRepository;
    private final StudentRepository studentRepository;
    private final UniversityRepository universityRepository;

    private final StoreUniversityService storeUniversityService;
    private final StudentService studentService;
    private final OwnerService ownerService;

    private final S3StorageService s3StorageService;
    private final BannerRepository bannerRepository;
    private final SignatureMenuRepository signatureMenuRepository;

    private final GoogleCloudOCRService googleCloudOCRService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String DEFAULT_BANNER_URL = "https://bab-e-deuk-bucket.s3.ap-northeast-2.amazonaws.com/Default/default_banner.png";

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
    public StoreResponseDto.StoreCreateResultDto createStore(Long ownerId, StoreRequestDto.StoreCreateRequestDto requestDto, MultipartFile bannerFile){
        Owner findOwner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerHandler(ErrorStatus.OWNER_NOT_FOUND));

        Store newStore = StoreConverter.toStore(findOwner, requestDto);
        storeRepository.save(newStore);

        storeUniversityService.saveStoreUniversity(newStore, requestDto.getUniversity());

        if (bannerFile != null && !bannerFile.isEmpty()){
            try{
                String bannerUrl = s3StorageService.uploadFile(bannerFile, "Banners");
                Banner banner = Banner.builder()
                        .bannerName(bannerFile.getOriginalFilename())
                        .bannerType(bannerFile.getContentType())
                        .bannerUrl(bannerUrl)
                        .store(newStore)
                        .build();

                newStore.setBanner(banner);

                bannerRepository.save(banner);
            }catch (IOException e){
                throw new ImageHandler(ErrorStatus.FILE_UPLOAD_FAILED);
            }
        }

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

        University findUniversity;

        String role = findStudentOrOwner(param.getAuthorizationHeader());

        if (role.equals("student")) {

            Long studentId = studentService.getUserIdFromJwt(param.getAuthorizationHeader());

            Student findStudent = studentRepository.findById(studentId)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            findUniversity = findStudent.getUniversity();

            if (findUniversity == null) {

                throw new UniversityHandler(ErrorStatus.UNIVERSITY_NOT_SETTING);
            }
        }
        else {

            Long ownerId = ownerService.getOwnerIdFromJwt(param.getAuthorizationHeader());

            Owner findOwner = ownerRepository.findById(ownerId)
                    .orElseThrow(() -> new OwnerHandler(ErrorStatus.OWNER_NOT_FOUND));

            if (findOwner.getStoreList().isEmpty()) {

                throw new OwnerHandler(ErrorStatus.OWNER_NOT_HAVE_STORE);
            }

            Store ownerStore = findOwner.getStoreList().get(0);

            if (ownerStore.getStoreUniversityList().isEmpty()) {

                throw new UniversityHandler(ErrorStatus.UNIVERSITY_NOT_SETTING);
            }

            findUniversity = ownerStore.getStoreUniversityList().get(0).getUniversity();
        }

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

        University findUniversity;

        String role = findStudentOrOwner(param.getAuthorizationHeader());

        if (role.equals("student")) {

            Long studentId = studentService.getUserIdFromJwt(param.getAuthorizationHeader());

            Student findStudent = studentRepository.findById(studentId)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            findUniversity = findStudent.getUniversity();
            if (findUniversity == null) {

                findUniversity = universityRepository.findByUniversityName("숭실대학교")
                        .orElseThrow(() -> new UniversityHandler(ErrorStatus.UNIVERSITY_NOT_FOUND));
            }
        }
        else {

            Long ownerId = ownerService.getOwnerIdFromJwt(param.getAuthorizationHeader());

            Owner findOwner = ownerRepository.findById(ownerId)
                    .orElseThrow(() -> new OwnerHandler(ErrorStatus.OWNER_NOT_FOUND));

            List<Store> ownerStore = findOwner.getStoreList();

            if (ownerStore.isEmpty()) {

                // 만약 사장님이 가게가 없을 경우 default 로 숭실대를 보여주는 것으로 설정했습니다.
                findUniversity = universityRepository.findByUniversityName("숭실대학교")
                        .orElseThrow(() -> new UniversityHandler(ErrorStatus.UNIVERSITY_NOT_FOUND));
            }
            else {

                // 일단 가게마다 무조건 대학교가 하나 걸려있다고 가정하에 짠 코드라 후에 수정해야할 듯 합니다.
                // 애초에 현재 erd 상으로는 대학 : 가게 가 n : n 인데 데모데이까지는 1 : n 으로 짜여져 있어 일단 이렇게 놔두었습니다.
                if (ownerStore.get(0).getStoreUniversityList().isEmpty()) {

                    findUniversity = universityRepository.findByUniversityName("숭실대학교")
                            .orElseThrow(() -> new UniversityHandler(ErrorStatus.UNIVERSITY_NOT_FOUND));
                }
                else {

                    findUniversity = ownerStore.get(0).getStoreUniversityList().get(0).getUniversity();
                }
            }
        }

        List<StoreUniversity> storeUniversityList = storeUniversityRepository.findAllByUniversity(findUniversity)
                .orElse(new ArrayList<>());

        List<Store> storeList = storeUniversityList.stream()
                .map(StoreUniversity::getStore)
                .toList();

        return storeList.stream()
                .map(store -> {
                    // 할인을 하는 가게
                    if (store.getDiscountList().stream().anyMatch(Discount::getInProgress)) {
                        // 할인을 하는 가게 + 대표 메뉴가 설정되어 있는 가게
                        if (store.getSignatureMenu() != null) {

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
                        else { // 할인을 하는 가게 + 대표 메뉴가 설정되어 있지 않은 가게

                            DiscountMenu discountMenu = discountMenuRepository.GetDiscountMenuByStoreAndMaxDiscountPrice(store);

                            return StoreConverter.toStoreDataDto(store, discountMenu.getMenu(), discountMenu.getDiscountPrice());
                        }
                    }
                    else { // 할인을 하지 않는 가게

                        if (store.getSignatureMenu() != null) { // 할인을 하지 않는 가게 + 대표 메뉴가 설정되어 있는 가게

                            return StoreConverter.toStoreDataDto(store, store.getSignatureMenu().getMenu(), 0);
                        }
                        else { // 할인을 하지 않는 가게 + 대표 메뉴가 설정되어 있지 않은 가게

                            Menu menu = menuRepository.GetMaxPriceMenuByStore(store);

                            return StoreConverter.toStoreDataDto(store, menu, 0);
                        }
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

        log.info("datas size: {}", datas.size());

        if (datas.get(2).equals("")){
            throw new StoreHandler(ErrorStatus.OCR_BAD_REQUEST);
        }

        return StoreConverter.toCertificateResultDto(datas);
    }

    public List<StoreResponseDto.GetStoreSearchDto> searchStores(StoreParameterDto.GetSearchKeywordParamDto searchKeywordParamDto) {
        Long userId = extractUserIdBasedOnRole(searchKeywordParamDto.getAuthorizationHeader());

        if (searchKeywordParamDto.getLatitude() != null && searchKeywordParamDto.getLongitude() != null) {
            return searchStoreWithMenusByCoordinates(searchKeywordParamDto, searchKeywordParamDto.getLatitude(), searchKeywordParamDto.getLongitude());
        } else {
            return searchStoreWithMenus(searchKeywordParamDto, userId);
        }
    }

    @Cacheable(value = "storeSearch", key = "#param.keyword")
    public List<StoreResponseDto.GetStoreSearchDto> searchStoreWithMenus(
            StoreParameterDto.GetSearchKeywordParamDto param,
            Long studentId
    ) {
        String keyword = param.getKeyword();
        Student student = studentService.findStudentById(studentId);
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

    @Cacheable(value = "storeSearch", key = "#param.keyword + ':' + T(java.lang.Math).round(#latitude * 10) / 10 + ':' + T(java.lang.Math).round(#longitude * 10) / 10")
    public List<StoreResponseDto.GetStoreSearchDto> searchStoreWithMenusByCoordinates(
            StoreParameterDto.GetSearchKeywordParamDto param,
            Double latitude,
            Double longitude
    ){
        String keyword = param.getKeyword();
        double radius = 5.0; // 반경 (km)

        // 반경 내의 가게 검색
        List<Store> stores = storeRepository.findStoresByMenuKeywordAndCoordinates(keyword);

        return stores.stream()
                .map(store -> {
                    double distance = calculateDistance(latitude, longitude, store.getLatitude(), store.getLongitude());

                    // 반경 내의 가게만 필터링
                    if (distance <= radius) {
                        return StoreConverter.toStoreSearchResponseDto(store, keyword, distance);
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull) // null 값 필터링
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
    public Store getStore(Long storeId){
        return storeRepository.findById(storeId).orElseThrow(() -> new StoreHandler(ErrorStatus.STORE_NOT_FOUND));
    }

    public String getStoreBannerUrl(Store store){
        String bannerUrl = null;
        if(store.getBanner() != null) {
            Banner storeBanner = bannerRepository.findByStore(store);
            bannerUrl = storeBanner.getBannerUrl();
        } else {
            bannerUrl = DEFAULT_BANNER_URL;
        }


        return bannerUrl;
    }

    public University getStoreUniversity(Store store){
        StoreUniversity storeUniversity = storeUniversityRepository.findByStoreId(store.getId()).orElseThrow(()-> new UniversityHandler(ErrorStatus.STORE_UNIVERSITY_NOT_FOUND));

        return  storeUniversity.getUniversity();
    }

    public List<OperatingHours> getOperatingHours(Long storeId){
        Store store = storeRepository.findById(storeId).orElseThrow(()-> new StoreHandler(ErrorStatus.STORE_NOT_FOUND));
        return store.getOperatingHoursList();
    }

    public List<Menu> getStoreMenu(Long storeId){
        Store store = storeRepository.findById(storeId).orElseThrow(()->new StoreHandler(ErrorStatus.STORE_NOT_FOUND));

        return store.getMenuList();
    }

    public SignatureMenu getSignatureMenu(Long storeId){
        Store store = storeRepository.findById(storeId).orElseThrow(()->new StoreHandler(ErrorStatus.STORE_NOT_FOUND));

        return store.getSignatureMenu();
    }

    private Long extractUserIdBasedOnRole(String authorizationHeader) {
        String role = jwtTokenProvider.getRole(authorizationHeader.substring(7));

        if ("student".equals(role)) {
            return studentService.getUserIdFromJwt(authorizationHeader);
        } else {
            return ownerService.getOwnerIdFromJwt(authorizationHeader);
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private String findStudentOrOwner(String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new GeneralException(ErrorStatus.MISSING_JWT_EXCEPTION);
        }

        return jwtTokenProvider.getRole(authorizationHeader.substring(7));
    }
}
