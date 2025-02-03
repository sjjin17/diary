package todaktodak.domain.auth.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import todaktodak.domain.auth.dto.SocialUserInfo;
import todaktodak.domain.user.domain.SocialType;
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
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class NaverLoginStrategyTest {
    @Value("${oauth.naver.token-uri}")
    private String NAVER_TOKEN_URI;
    @Value("${oauth.naver.userinfo-uri}")
    private String NAVER_USERINFO_URI;

    @Autowired
    private NaverLoginStrategy naverLoginStrategy;
    @Autowired
    private RestTemplate restTemplate;


    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }


    @Test
    void 네이버_로그인_성공() throws JsonProcessingException {
        String expectedAccessToken = "accessToken";
        SocialUserInfo expectedUserInfo = SocialUserInfo.builder()
                .socialId("1234n")
                .email("username@naver.com")
                .name("username")
                .imageUrl("imgUrl")
                .socialType(SocialType.NAVER)
                .build();

        mockServer.expect(requestTo(NAVER_TOKEN_URI))
                .andExpect(content().contentType("application/x-www-form-urlencoded;charset=utf-8"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(String.format("{\"access_token\":\"%s\"}", expectedAccessToken), MediaType.APPLICATION_JSON));

        String response = String.format("{\"response\": {\"id\": \"%s\", \"email\": \"%s\", \"name\": \"%s\",\"profile_image\": \"%s\"}}",
                expectedUserInfo.socialId(),
                expectedUserInfo.email(),
                expectedUserInfo.name(),
                expectedUserInfo.imageUrl());


        mockServer.expect(requestTo(NAVER_USERINFO_URI))
                .andExpect(header("Authorization", "Bearer " + expectedAccessToken))
                .andExpect(content().contentType("application/x-www-form-urlencoded;charset=utf-8"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));


        SocialUserInfo userInfo = naverLoginStrategy.login("authorizationCode", SocialType.NAVER);
        mockServer.verify();
        assertThat(userInfo).isEqualTo(expectedUserInfo);
    }

}