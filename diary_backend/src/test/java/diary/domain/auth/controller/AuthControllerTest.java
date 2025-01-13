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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;


    @MockitoBean
    AuthService authService;

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




}