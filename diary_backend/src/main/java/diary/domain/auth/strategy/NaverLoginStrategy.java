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

@Service(value="NAVER")
@RequiredArgsConstructor
public class NaverLoginStrategy implements LoginStrategy {
    private final RestTemplate restTemplate;

    @Value("${oauth.naver.client-id}")
    private String NAVER_CLIENT_ID;
    @Value("${oauth.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;
    @Value("${oauth.naver.redirect-uri}")
    private String NAVER_REDIRECT_URL;
    @Value("${oauth.naver.authorization-grant-type}")
    private String NAVER_GRANT_TYPE;
    @Value("${oauth.naver.state}")
    private String NAVER_STATE;
    @Value("${oauth.naver.token-uri}")
    private String NAVER_TOKEN_URI;
    @Value("${oauth.naver.userinfo-uri}")
    private String NAVER_USERINFO_URI;

    @Override
    public SocialUserInfo login(String code, SocialType socialType) throws JsonProcessingException {
        String token = getAccessToken(code, socialType);
        SocialUserInfo socialUserInfo = getUserInfo(token, socialType);
        return socialUserInfo;

    }

    private String getAccessToken(String code, SocialType socialType) throws JsonProcessingException {
        // HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", NAVER_GRANT_TYPE);
        body.add("client_id", NAVER_CLIENT_ID);
        body.add("client_secret", NAVER_CLIENT_SECRET);
        body.add("redirect_uri", NAVER_REDIRECT_URL);
        body.add("code", code);
        body.add("state", NAVER_STATE);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                NAVER_TOKEN_URI,
                HttpMethod.POST,
                tokenRequest,
                String.class
        );

        // response ->  엑세스 토큰 파싱
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
                NAVER_USERINFO_URI,
                HttpMethod.GET,
                userInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody).get("response");
        return SocialUserInfo.builder()
                .socialId(jsonNode.get("id").asText())
                .email(jsonNode.get("email").asText())
                .name(jsonNode.get("name").asText())
                .imageUrl(jsonNode.get("profile_image").asText())
                .socialType(socialType)
                .build();

    }
}
