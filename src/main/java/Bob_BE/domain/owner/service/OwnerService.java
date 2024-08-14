package Bob_BE.domain.owner.service;

import Bob_BE.domain.owner.dto.parameter.OwnerParameterDto;
import Bob_BE.domain.owner.dto.request.OwnerRequestDto;
import Bob_BE.domain.owner.dto.response.OwnerResponseDto;
import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.owner.repository.OwnerRepository;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.repository.StoreRepository;
import Bob_BE.global.external.KakaoAuthClient;
import Bob_BE.global.external.KakaoResponseDto;
import Bob_BE.global.external.KakaoUserClient;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.code.resultCode.SuccessStatus;
import Bob_BE.global.response.exception.GeneralException;
import Bob_BE.global.response.exception.handler.OwnerHandler;
import Bob_BE.global.util.JwtTokenProvider;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoUserClient kakaoUserClient;
    private final KakaoAuthClient kakaoAuthClient;
    private final StoreRepository storeRepository;
    private final String ROLE = "owner";

    @Value("${social.client.kakao.rest-api-key}")
    private String kakaoAppKey;
    @Value("${social.client.kakao.secret-key}")
    private String kakaoAppSecret;
    @Value("${social.client.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${social.client.kakao.grant_type}")
    private String kakaoGrantType;

    @Transactional
    public OwnerResponseDto.LoginOrRegisterDto registerOrLogin(OwnerRequestDto.LoginOrRegisterDto request) {
        try {
            String authorizationCode = request.getToken();
            KakaoResponseDto.KakaoTokenResponseDto kakaoTokenInfo = kakaoAuthClient.getAccessToken(
                    kakaoAppKey,
                    kakaoAppSecret,
                    kakaoGrantType,
                    kakaoRedirectUri,
                    authorizationCode
            );

            String authorization = "Bearer " + kakaoTokenInfo.getAccessToken();
            KakaoResponseDto.KakaoUserResponseDto kakaoUserInfo = kakaoUserClient.getUserInfo(authorization);

            Long socialId = kakaoUserInfo.getId();
            String email = kakaoUserInfo.getKakao_account().getEmail();
            String nickname = kakaoUserInfo.getProperties().getNickname();

            Optional<Owner> existingOwner = ownerRepository.findBySocialId(socialId);

            if (existingOwner.isPresent()) {
                String jwt = jwtTokenProvider.createToken(existingOwner.get().getId(), ROLE);
                return OwnerResponseDto.LoginOrRegisterDto.builder()
                        .jwt(jwt)
                        .successStatus(SuccessStatus._OK)
                        .build();
            } else {
                Owner newOwner = Owner.builder()
                        .socialId(socialId)
                        .email(email)
                        .nickname(nickname)
                        .build();
                Owner savedOwner = ownerRepository.save(newOwner);
                String jwt = jwtTokenProvider.createToken(savedOwner.getId(), ROLE);
                return OwnerResponseDto.LoginOrRegisterDto.builder()
                        .jwt(jwt)
                        .successStatus(SuccessStatus._CREATED)
                        .build();
            }
        } catch (FeignException.Unauthorized e){
            log.error("Unauthorized error during Kakao API call", e);
            throw new GeneralException(ErrorStatus.KAKAO_TOKEN_ERROR);
        } catch (Exception e) {
            log.error("Error during student register or login", e);
            throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Long getOwnerIdFromJwt(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new GeneralException(ErrorStatus.MISSING_JWT_EXCEPTION);
        }
        String jwtToken = authorizationHeader.substring(7);
        boolean isValidToken = jwtTokenProvider.isValidateToken(jwtToken);
        if (!isValidToken) {
            throw new GeneralException(ErrorStatus.EXPIRED_JWT_EXCEPTION);
        }
        return jwtTokenProvider.getId(jwtToken);
    }

    /**
     * 사장님 마이페이지 API
     * 사장님 데이터 찾는 메서드
     * return : Owner
     */
    public Owner getOwnerMypage(OwnerParameterDto.OwnerMyPageParamDto param) {

        Long ownerId = getOwnerIdFromJwt(param.getAuthorizationHeader());

        return ownerRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerHandler(ErrorStatus.OWNER_NOT_FOUND));
    }

    /**
     * 사장님 마이페이지 API
     * 사장님이 가게를 보유하고있는지 확인하는 메서드
     * return : Store
     */
    public Store getOwnerStore(Owner owner) {

        return storeRepository.findFirstByOwnerId(owner.getId())
                .orElse(new Store());
    }
}
