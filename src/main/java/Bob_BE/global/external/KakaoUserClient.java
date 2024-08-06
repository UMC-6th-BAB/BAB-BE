package Bob_BE.global.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoUser" , url = "https://kapi.kakao.com/v2/user/me")
public interface KakaoUserClient {
    @GetMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoResponseDto.KakaoUserResponseDto getUserInfo(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    );
}