package Bob_BE.domain.student.service;

import Bob_BE.domain.student.dto.request.StudentRequestDto;
import Bob_BE.domain.student.dto.response.StudentResponseDto;
import Bob_BE.domain.student.entity.Student;
import Bob_BE.domain.student.repository.StudentRepository;
import Bob_BE.domain.student.util.JwtTokenProvider;
import Bob_BE.global.external.KakaoResponseDto;
import Bob_BE.global.external.KakaoTokenClient;
import Bob_BE.global.external.KakaoUserClient;
import Bob_BE.global.response.code.resultCode.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    @Value("${security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grandType;

    private final StudentRepository studentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoTokenClient kakaoTokenClient;
    private final KakaoUserClient kakaoUserClient;

    public StudentResponseDto.LoginOrRegisterDto registerOrLogin(StudentRequestDto.LoginOrRegisterDto request) {
        String token = request.getToken();
        String authorization = "Bearer " + kakaoTokenClient.getAccessToken(grandType, clientId ,redirectUri ,token)
                .getAccess_token();
        KakaoResponseDto.KakaoUserResponseDto kakaoUserInfo = kakaoUserClient.getUserInfo(authorization);

        String socialId = kakaoUserInfo.getSocialId();
        String email = kakaoUserInfo.getEmail();
        String nickname = kakaoUserInfo.getNickname();

        Optional<Student> existingStudent = studentRepository.findBySocialId(socialId);

        if (existingStudent.isPresent()) {
            String jwt = jwtTokenProvider.createToken(existingStudent.get().getId());
            return StudentResponseDto.LoginOrRegisterDto.builder()
                    .jwt(jwt)
                    .successStatus(SuccessStatus._OK)
                    .build();
        } else {
            Student newStudent = Student.builder()
                    .socialId(socialId)
                    .email(email)
                    .nickname(nickname)
                    .build();
            Student savedStudent = studentRepository.save(newStudent);
            String jwt = jwtTokenProvider.createToken(savedStudent.getId());
            return StudentResponseDto.LoginOrRegisterDto.builder()
                    .jwt(jwt)
                    .successStatus(SuccessStatus._CREATED)
                    .build();
        }
    }
}
