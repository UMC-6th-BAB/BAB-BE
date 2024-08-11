package Bob_BE.global.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KakaoAuth" , url = "https://kauth.kakao.com")
public interface KakaoAuthClient {
    @PostMapping(path = "/oauth/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=utf-8")
    KakaoResponseDto.KakaoTokenResponseDto getAccessToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String authorizationCode
    );
}
