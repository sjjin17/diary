package diary.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import diary.domain.auth.dto.SocialUserInfo;
import diary.domain.auth.dto.Token;
import diary.domain.auth.dto.response.TokenResponseDto;
import diary.domain.auth.strategy.GoogleLoginStrategy;
import diary.domain.auth.strategy.LoginStrategy;
import diary.domain.user.domain.SocialType;
import diary.domain.user.domain.User;
import diary.domain.user.fixture.UserFixture;
import diary.domain.user.repository.UserRepository;
import diary.domain.user.service.UserService;
import diary.global.config.jwt.JwtProvider;
import diary.global.exception.CustomException;
import diary.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
class AuthServiceTest {

    @Mock
    private Map<String, LoginStrategy> loginStrategyMap;

    @Mock
    JwtProvider jwtProvider;

    @Mock
    UserRepository userRepository;

    @Mock
    private UserService userService;
    @InjectMocks
    private AuthService authService;



    @Test
    void 이미_존재하는_회원의_구글_로그인_성공() throws Exception {
        // Given
        String provider = "GOOGLE";
        SocialUserInfo expectedUserInfo = SocialUserInfo.builder()
                .socialId("1234")
                .email("username@gmail.com")
                .name("username")
                .imageUrl("imgUrl")
                .socialType(SocialType.GOOGLE)
                .build();
        User user = UserFixture.createUser(SocialType.GOOGLE);
        Token expectedToken = Token.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
        LoginStrategy googleLoginStrategy = mock(GoogleLoginStrategy.class);


        given(googleLoginStrategy.login(anyString(), eq(SocialType.GOOGLE))).willReturn(expectedUserInfo);
        given(loginStrategyMap.get(provider)).willReturn(googleLoginStrategy);
        given(userRepository.findBySocialIdAndSocialType(expectedUserInfo.socialId(), expectedUserInfo.socialType())).willReturn(Optional.of(user));
        given(jwtProvider.generateAccessToken(user)).willReturn("accessToken");
        given(jwtProvider.generateRefreshToken()).willReturn("refreshToken");

        // When
        Token token = authService.login("authorizationCode", provider);


        // Then
        verify(userService, never()).join(any(SocialUserInfo.class), anyString());
        assertThat(token).isEqualTo(expectedToken);


    }

    @Test
    void 신규_회원_구글_로그인() throws JsonProcessingException {
        // Given
        String provider = "GOOGLE";
        SocialUserInfo expectedUserInfo = SocialUserInfo.builder()
                .socialId("1234")
                .email("username@gmail.com")
                .name("username")
                .imageUrl("imgUrl")
                .socialType(SocialType.GOOGLE)
                .build();
        User user = UserFixture.createUser(SocialType.GOOGLE);
        Token expectedToken = Token.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
        LoginStrategy googleLoginStrategy = mock(GoogleLoginStrategy.class);


        given(googleLoginStrategy.login(anyString(), eq(SocialType.GOOGLE))).willReturn(expectedUserInfo);
        given(loginStrategyMap.get(provider)).willReturn(googleLoginStrategy);
        given(userRepository.findBySocialIdAndSocialType(expectedUserInfo.socialId(), expectedUserInfo.socialType())).willReturn(Optional.empty());
        given(userService.join(expectedUserInfo, expectedToken.refreshToken())).willReturn(user);
        given(jwtProvider.generateAccessToken(user)).willReturn("accessToken");
        given(jwtProvider.generateRefreshToken()).willReturn("refreshToken");

        // When
        Token token = authService.login("authorizationCode", provider);


        // Then
        verify(userService).join(any(SocialUserInfo.class), anyString());
        assertThat(token).isEqualTo(expectedToken);
    }


    @Test
    void 리프레시_토큰_만료일이_3일_미만일_때_토큰_재발급_성공() {
        // given
        String newRefreshToken = "newRefreshToken";
        User user = UserFixture.createUser(SocialType.GOOGLE);
        Token expectedToken = Token.builder()
                .accessToken("accessToken")
                .refreshToken(newRefreshToken)
                .build();
        given(userRepository.findByRefreshToken(anyString())).willReturn(Optional.of(user));
        given(jwtProvider.generateAccessToken(user)).willReturn("accessToken");
        given(jwtProvider.checkRefreshTokenExpiration(anyString())).willReturn((long) 1000 * 60 * 60 * 24);
        given(jwtProvider.generateRefreshToken()).willReturn(newRefreshToken);


        // when
        Token token = authService.reissue("refreshToken");

        // then
        assertThat(token).isEqualTo(expectedToken);
        assertThat(user.getRefreshToken()).isEqualTo(newRefreshToken);

    }

    @Test
    void 리프레시_토큰_만료일_3일_이상일_때_토큰_재발급_성공() {
        // given
        User user = UserFixture.createUser(SocialType.GOOGLE);
        Token expectedToken = Token.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
        given(userRepository.findByRefreshToken(anyString())).willReturn(Optional.of(user));
        given(jwtProvider.generateAccessToken(user)).willReturn("accessToken");
        given(jwtProvider.checkRefreshTokenExpiration(anyString())).willReturn((long) 1000 * 60 * 60 * 24 * 4);


        // when
        Token token = authService.reissue("refreshToken");

        // then
        assertThat(token).isEqualTo(expectedToken);

    }

    @Test
    void 리프레시_토큰_불일치로_재발급_실패() {
        // given
        String refreshToken = "refreshToken";
        given(userRepository.findByRefreshToken(refreshToken)).willReturn(Optional.empty());



        // when, then
        CustomException e = assertThrows(CustomException.class, () -> {
            authService.reissue(refreshToken);
        });

        assertEquals(ErrorCode.USER_NOT_EXISTS, e.getErrorCode());


    }



}