package Bob_BE.domain.student.service;

import Bob_BE.domain.student.dto.request.StudentRequestDto;
import Bob_BE.domain.student.dto.response.StudentResponseDto;
import Bob_BE.domain.student.entity.Student;
import Bob_BE.domain.student.repository.StudentRepository;
import Bob_BE.domain.student.util.JwtTokenProvider;
import Bob_BE.global.external.KakaoResponseDto;
import Bob_BE.global.external.KakaoUserClient;
import Bob_BE.global.response.code.resultCode.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoUserClient kakaoUserClient;

    public StudentResponseDto.LoginOrRegisterDto registerOrLogin(StudentRequestDto.LoginOrRegisterDto request) {
        String authorization = "Bearer " + request.getToken();
        KakaoResponseDto.KakaoUserResponseDto kakaoUserInfo = kakaoUserClient.getUserInfo(authorization);

        Long socialId = kakaoUserInfo.getId();
        String email = kakaoUserInfo.getKakao_account().getEmail();
        String nickname = kakaoUserInfo.getProperties().getNickname();

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
