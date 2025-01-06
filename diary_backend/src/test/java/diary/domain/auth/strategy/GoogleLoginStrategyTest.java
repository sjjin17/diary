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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GoogleLoginStrategyTest {

    @Value("${oauth.google.token-uri}")
    private String GOOGLE_TOKEN_URI;
    @Value("${oauth.google.userinfo-uri}")
    private String GOOGLE_USERINFO_URI;

    @Autowired
    private GoogleLoginStrategy googleLoginStrategy;
    @Autowired
    private RestTemplate restTemplate;


    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void 구글_로그인_성공() throws JsonProcessingException {

        String expectedAccessToken = "accessToken";
        SocialUserInfo expectedUserInfo = SocialUserInfo.builder()
                .socialId("1234g")
                .email("username@gmail.com")
                .name("username")
                .imageUrl("imgUrl")
                .socialType(SocialType.GOOGLE)
                .build();

        mockServer.expect(requestTo(GOOGLE_TOKEN_URI))
                .andExpect(content().contentType("application/x-www-form-urlencoded;charset=utf-8"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(String.format("{\"access_token\":\"%s\"}", expectedAccessToken), MediaType.APPLICATION_JSON));

        String response = String.format("{\"name\": \"%s\", \"email\": \"%s\", \"id\": \"%s\", \"picture\": \"%s\"}",
                expectedUserInfo.name(),
                expectedUserInfo.email(),
                expectedUserInfo.socialId(),
                expectedUserInfo.imageUrl());





        mockServer.expect(requestTo(GOOGLE_USERINFO_URI))
                .andExpect(header("Authorization", "Bearer " + expectedAccessToken))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));


        SocialUserInfo userInfo = googleLoginStrategy.login("authorizationCode", SocialType.GOOGLE);
        mockServer.verify();
        assertThat(userInfo).isEqualTo(expectedUserInfo);
    }





}