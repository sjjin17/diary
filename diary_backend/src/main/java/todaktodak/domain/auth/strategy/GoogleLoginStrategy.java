package todaktodak.domain.auth.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import todaktodak.domain.auth.dto.SocialUserInfo;
import todaktodak.domain.user.domain.SocialType;
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

@Service(value = "GOOGLE")
@RequiredArgsConstructor
public class GoogleLoginStrategy implements LoginStrategy {

    private final RestTemplate restTemplate;

    @Value("${oauth.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${oauth.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${oauth.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URL;
    @Value("${oauth.google.scope}")
    private String GOOGLE_SCOPE;
    @Value("${oauth.google.authorization-grant-type}")
    private String GOOGLE_GRANT_TYPE;
    @Value("${oauth.google.token-uri}")
    private String GOOGLE_TOKEN_URI;
    @Value("${oauth.google.userinfo-uri}")
    private String GOOGLE_USERINFO_URI;


    public SocialUserInfo login(String code, SocialType socialType) throws JsonProcessingException {
        String accessToken = getAccessToken(code, socialType);
        SocialUserInfo socialUserInfo = getUserInfo(accessToken, socialType);
        return socialUserInfo;
    }



    private String getAccessToken(String code, SocialType socialType) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", GOOGLE_GRANT_TYPE);
        body.add("client_id", GOOGLE_CLIENT_ID);
        body.add("client_secret", GOOGLE_CLIENT_SECRET);
        body.add("redirect_uri", GOOGLE_REDIRECT_URL);
        body.add("code", code);
        body.add("scope", GOOGLE_SCOPE);


        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_TOKEN_URI,
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
        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_USERINFO_URI,
                HttpMethod.GET,
                userInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return SocialUserInfo.builder()
                .socialId(jsonNode.get("id").asText())
                .email(jsonNode.get("email").asText())
                .name(jsonNode.get("name").asText())
                .imageUrl(jsonNode.get("picture").asText())
                .socialType(socialType)
                .build();
    }
}
