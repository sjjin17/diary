package diary.domain.auth.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import diary.domain.auth.dto.SocialUserInfo;
import diary.domain.user.domain.SocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class KakaoLoginStrategyTest {
    @Value("${oauth.kakao.token-uri}")
    private String KAKAO_TOKEN_URI;
    @Value("${oauth.kakao.userinfo-uri}")
    private String KAKAO_USERINFO_URI;

    @Autowired
    private KakaoLoginStrategy kakaoLoginStrategy;
    @Autowired
    private RestTemplate restTemplate;


    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }


    @Test
    void 카카오_로그인_성공() throws JsonProcessingException {
        String expectedAccessToken = "accessToken";
        SocialUserInfo expectedUserInfo = SocialUserInfo.builder()
                .socialId("1234k")
                .email("username@kakao.com")
                .name("username")
                .imageUrl("imgUrl")
                .socialType(SocialType.KAKAO)
                .build();

        mockServer.expect(requestTo(KAKAO_TOKEN_URI))
                .andExpect(content().contentType("application/x-www-form-urlencoded;charset=utf-8"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(String.format("{\"access_token\":\"%s\"}", expectedAccessToken), MediaType.APPLICATION_JSON));

        String response = String.format("{\"properties\": {\"nickname\": \"%s\", \"profile_image\": \"%s\"}, \"kakao_account\": {\"email\": \"%s\"}, \"id\": \"%s\"}",
                expectedUserInfo.name(),
                expectedUserInfo.imageUrl(),
                expectedUserInfo.email(),
                expectedUserInfo.socialId());


        mockServer.expect(requestTo(KAKAO_USERINFO_URI))
                .andExpect(header("Authorization", "Bearer " + expectedAccessToken))
                .andExpect(content().contentType("application/x-www-form-urlencoded;charset=utf-8"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));


        SocialUserInfo userInfo = kakaoLoginStrategy.login("authorizationCode", SocialType.KAKAO);
        mockServer.verify();
        assertThat(userInfo).isEqualTo(expectedUserInfo);
    }

}