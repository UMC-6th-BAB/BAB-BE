package Bob_BE.global.external;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KakaoToken" , url = "https://kauth.kakao.com/oauth/token")
public interface KakaoTokenClient {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoResponseDto.KakaoTokenResponseDto getAccessToken(@RequestParam("grant_type") String grantType,
                                                          @RequestParam("client_id") String clientId,
                                                          @RequestParam("redirect_uri") String redirectUri,
                                                          @RequestParam("code") String code
    );
}