package Bob_BE.domain.owner.service;

import Bob_BE.domain.owner.dto.request.OwnerRequestDto;
import Bob_BE.domain.owner.dto.response.OwnerResponseDto;
import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.owner.repository.OwnerRepository;
import Bob_BE.global.external.KakaoResponseDto;
import Bob_BE.global.external.KakaoUserClient;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.code.resultCode.SuccessStatus;
import Bob_BE.global.response.exception.GeneralException;
import Bob_BE.global.util.JwtTokenProvider;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public OwnerResponseDto.LoginOrRegisterDto registerOrLogin(OwnerRequestDto.LoginOrRegisterDto request) {
        try {
            String authorization = "Bearer " + request.getToken();
            KakaoResponseDto.KakaoUserResponseDto kakaoUserInfo = kakaoUserClient.getUserInfo(authorization);

            Long socialId = kakaoUserInfo.getId();
            String email = kakaoUserInfo.getKakao_account().getEmail();
            String nickname = kakaoUserInfo.getProperties().getNickname();

            Optional<Owner> existingOwner = ownerRepository.findBySocialId(socialId);

            if (existingOwner.isPresent()) {
                String jwt = jwtTokenProvider.createToken(existingOwner.get().getId());
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
                String jwt = jwtTokenProvider.createToken(savedOwner.getId());
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
}
