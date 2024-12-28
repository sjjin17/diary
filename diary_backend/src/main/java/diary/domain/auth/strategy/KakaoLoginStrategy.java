package diary.domain.auth.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import diary.domain.auth.dto.SocialUserInfo;
import diary.domain.user.domain.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service(value="KAKAO")
@RequiredArgsConstructor
public class KakaoLoginStrategy implements LoginStrategy {

    private final RestTemplate restTemplate;

    @Value("${oauth.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${oauth.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;
    @Value("${oauth.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URL;
    @Value("${oauth.kakao.authorization-grant-type}")
    private String KAKAO_GRANT_TYPE;
    @Value("${oauth.kakao.token-uri}")
    private String KAKAO_TOKEN_URI;
    @Value("${oauth.kakao.userinfo-uri}")
    private String KAKAO_USERINFO_URI;

    @Override
    public SocialUserInfo login(String code, SocialType socialType) throws JsonProcessingException {
        String token = getAccessToken(code, socialType);
        SocialUserInfo userInfo = getUserInfo(token, socialType);
        return userInfo;
    }

    private String getAccessToken(String code, SocialType socialType) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", KAKAO_GRANT_TYPE);
        body.add("client_id", KAKAO_CLIENT_ID);
        body.add("redirect_uri", KAKAO_REDIRECT_URL);
        body.add("code", code);
        body.add("client_secret", KAKAO_CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_TOKEN_URI,
                HttpMethod.POST,
                tokenRequest,
                String.class
        );


        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private SocialUserInfo getUserInfo(String accessToken, SocialType socialType) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_USERINFO_URI,
                HttpMethod.GET,
                userInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return SocialUserInfo.builder()
                .socialId(jsonNode.get("id").asText())
                .email(jsonNode.get("kakao_account").get("email").asText())
                .name(jsonNode.get("properties").get("nickname").asText())
                .imageUrl(jsonNode.get("properties").get("profile_image").asText())
                .socialType(socialType)
                .build();

    }
}
