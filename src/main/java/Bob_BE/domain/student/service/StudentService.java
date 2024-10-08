package Bob_BE.domain.student.service;

import Bob_BE.domain.store.converter.StoreConverter;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.repository.StoreRepository;
import Bob_BE.domain.student.converter.StudentConverter;
import Bob_BE.domain.student.dto.request.StudentRequestDto;
import Bob_BE.domain.student.dto.response.StudentResponseDto;
import Bob_BE.domain.student.entity.Student;
import Bob_BE.domain.student.repository.StudentRepository;
import Bob_BE.domain.university.repository.UniversityRepository;
import Bob_BE.global.external.KakaoAuthClient;
import Bob_BE.global.response.exception.handler.StudentHandler;
import Bob_BE.global.external.KakaoAuthClient;
import Bob_BE.global.util.JwtTokenProvider;
import Bob_BE.domain.university.entity.University;
import Bob_BE.global.external.KakaoResponseDto;
import Bob_BE.global.external.KakaoUserClient;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.code.resultCode.SuccessStatus;
import Bob_BE.global.response.exception.GeneralException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;
    private final UniversityRepository universityRepository;
    private final StoreRepository storeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoUserClient kakaoUserClient;
    private final KakaoAuthClient kakaoAuthClient;
    private final String ROLE = "student";

    @Value("${social.client.kakao.rest-api-key}")
    private String kakaoAppKey;
    @Value("${social.client.kakao.secret-key}")
    private String kakaoAppSecret;
    @Value("${social.client.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${social.client.kakao.grant_type}")
    private String kakaoGrantType;


    @Transactional
    public StudentResponseDto.LoginOrRegisterDto registerOrLogin(StudentRequestDto.LoginOrRegisterDto request) {
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

            Optional<Student> existingStudent = studentRepository.findBySocialId(socialId);


            if (existingStudent.isPresent()) {
                String jwt = jwtTokenProvider.createToken(existingStudent.get().getId(), ROLE);
                Boolean isUniversityExist = existingStudent.get().getUniversity() != null;
                return StudentResponseDto.LoginOrRegisterDto.builder()
                        .jwt(jwt)
                        .successStatus(SuccessStatus._OK)
                        .kakaoEmail(email)
                        .kakaoNickname(nickname)
                        .isUniversityExist(isUniversityExist)
                        .role(ROLE)
                        .build();
            } else {
                Student newStudent = Student.builder()
                        .socialId(socialId)
                        .email(email)
                        .nickname(nickname)
                        .build();
                Student savedStudent = studentRepository.save(newStudent);
                String jwt = jwtTokenProvider.createToken(savedStudent.getId(), ROLE);
                return StudentResponseDto.LoginOrRegisterDto.builder()
                        .jwt(jwt)
                        .successStatus(SuccessStatus._CREATED)
                        .kakaoEmail(email)
                        .kakaoNickname(nickname)
                        .isUniversityExist(false)
                        .role(ROLE)
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

    public Long getUserIdFromJwt(String authorizationHeader) {
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

    @Transactional
    public StudentResponseDto.myPageDto updateUniversity(Long userId, StudentRequestDto.updateUniversityDto request) {
        Long universityId = request.getUniversityId();
        University university = universityRepository.findById(universityId)
                .orElseThrow(()->new GeneralException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        Student student = studentRepository.findById(userId)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        student.setUniversity(university);
        studentRepository.save(student);

        return getMyPage(userId);
//        return StudentConverter.toUpdateUniversityDto(student);
    }

    public StudentResponseDto.myPageDto getMyPage(Long userId) {
        Student student = studentRepository.findById(userId)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        University university = student.getUniversity();
        List<StoreResponseDto.GetOnSaleStoreInMyPageDto> getOnSaleStoreDataDtos = null;
        if(university != null){
            List<StoreResponseDto.StoreAndDiscountDataDto> saleStoreAndDiscount = storeRepository.GetOnSaleStore(university);
            getOnSaleStoreDataDtos = StoreConverter.toGetOnSaleStoreInMyPageDtoList(saleStoreAndDiscount);
        }

        return StudentConverter.toMyPageDto(student, university, getOnSaleStoreDataDtos);
    }

    @Transactional(readOnly = true)
    public Student findStudentById(Long studentId){
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentHandler(ErrorStatus.STUDENT_NOT_FOUND));
    }
}
