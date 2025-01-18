package diary.domain.auth.controller;

import com.fasterxml.jackson.annotation.JsonFilter;
import diary.domain.auth.dto.SocialUserInfo;
import diary.domain.auth.dto.Token;
import diary.domain.auth.dto.response.TokenResponseDto;
import diary.domain.auth.service.AuthService;
import diary.domain.user.domain.SocialType;
import diary.domain.user.domain.User;
import diary.domain.user.fixture.UserFixture;
import diary.domain.user.repository.UserRepository;
import diary.domain.user.service.UserService;
import diary.global.WithMockCustomUser;
import diary.global.config.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@WebMvcTest(AuthController.class)
@AutoConfigureRestDocs
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {



    @Autowired
    MockMvc mockMvc;


    @MockitoBean
    AuthService authService;

    @MockitoBean
    JwtProvider jwtProvider;




    @Test
    @WithMockUser(username="user")
    void 로그인_성공() throws Exception {
        // given
        String code = "authorizationCode";
        String provider = "GOOGLE";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        SocialUserInfo expectedUserInfo = SocialUserInfo.builder()
                .socialId("1234g")
                .email("username@gmail.com")
                .name("username")
                .imageUrl("imgUrl")
                .socialType(SocialType.GOOGLE)
                .build();
        given(authService.login(code, provider)).willReturn(
                Token.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/auth/login")
                        .param("code", code)
                        .param("provider", provider))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))

                        .andExpect(jsonPath("$.data.accessToken").value(accessToken))
                        .andExpect(jsonPath("$.success").value(true))
                        .andExpect(header().string("Set-Cookie", containsString("refreshToken="+refreshToken)));

        // then
        resultActions.andDo(document("login",  preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("code").description("인가코드"),
                        parameterWithName("provider").description("소셜")
                ),
                responseFields(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("access token"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공")
                ))
        );

    }

    @Test
    @WithMockCustomUser
    void 토큰_재발급_성공() throws Exception {
        // given
        String newAccessToken = "newAccessToken";
        String refreshToken = "refreshToken";
        Token token = Token.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken)
                        .build();



        given(authService.reissue(refreshToken)).willReturn(token);
        given(jwtProvider.extractRefreshTokenFromCookie(any())).willReturn(refreshToken);
        given(jwtProvider.checkRefreshTokenExpiration(anyString())).willReturn(3600L);


        // when
        ResultActions resultActions = mockMvc.perform(
                        get("/auth/reissue")
                                .cookie(new Cookie("refreshToken", refreshToken)))

                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))

                .andExpect(jsonPath("$.data.accessToken").value(newAccessToken))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(header().string("Set-Cookie", containsString("refreshToken="+refreshToken)));

        // then
        resultActions.andDo(document("auth/reissue",  preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),

                responseFields(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("access token"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공")
                ))
        );
    }




}