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
import Bob_BE.global.response.exception.handler.*;

import Bob_BE.global.util.JwtTokenProvider;
import Bob_BE.global.util.aws.S3StorageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


import java.util.Map;

import Bob_BE.global.util.google.GoogleCloudOCRService;
import jakarta.validation.Valid;
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

        Long studentId = studentService.getUserIdFromJwt(param.getAuthorizationHeader());

        Student findStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        University findUniversity = findStudent.getUniversity();

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

        University findUniversity = new University();

        String role = jwtTokenProvider.getRole(param.getAuthorizationHeader());

        if (role.equals("student")) {

            Long studentId = studentService.getUserIdFromJwt(param.getAuthorizationHeader());

            Student findStudent = studentRepository.findById(studentId)
                    .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            findUniversity = findStudent.getUniversity();
        }
        else {

            Long ownerId = ownerService.getOwnerIdFromJwt(param.getAuthorizationHeader());

            Owner findOwner = ownerRepository.findById(ownerId)
                    .orElseThrow(() -> new OwnerHandler(ErrorStatus.OWNER_NOT_FOUND));

            List<Store> ownerStore = findOwner.getStoreList();

            if (ownerStore.isEmpty()) {

                // 만약 사장님이 가게가 없을 경우 default 로 숭실대를 보여주는 것으로 설정했습니다.
                findUniversity = universityRepository.findById(2L)
                        .get();
            }
            else {

                // 일단 가게마다 무조건 대학교가 하나 걸려있다고 가정하에 짠 코드라 후에 수정해야할 듯 합니다.
                // 애초에 현재 erd 상으로는 대학 : 가게 가 n : n 인데 데모데이까지는 1 : n 으로 짜여져 있어 일단 이렇게 놔두었습니다.
                findUniversity = ownerStore.get(0).getStoreUniversityList().get(0).getUniversity();
            }
        }

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

}
