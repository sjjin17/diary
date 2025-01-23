package todaktodak.domain.user.controller;

import org.apache.hc.core5.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import todaktodak.domain.auth.controller.AuthController;
import todaktodak.domain.user.dto.response.MyDiaryResponseDto;
import todaktodak.domain.user.service.UserService;
import todaktodak.global.WithMockCustomUser;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static todaktodak.domain.diary.fixture.DiaryFixture.MOCK_MY_DIARY_RESPONSE_DTO;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;


    private static final String ACCESS_TOKEN_PREFIX = "Bearer ";


    @Test
    @WithMockCustomUser
    void 참여중인_일기장_목록_조회() throws Exception {
        // given
        given(userService.getAllDiary(anyLong())).willReturn(List.of(MOCK_MY_DIARY_RESPONSE_DTO));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/users/me/diaries")
                .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "accessToken"));



        // then
        resultActions.andDo(document("user/getMyDiary", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                        fieldWithPath("data[].diaryId").type(JsonFieldType.NUMBER).description("다이어리 ID"),
                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("일기장 제목"),
                        fieldWithPath("data[].isShared").type(JsonFieldType.BOOLEAN).description("공유 여부"),
                        fieldWithPath("data[].thumbnail").type(JsonFieldType.STRING).description("썸네일"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data[0].diaryId").value(MOCK_MY_DIARY_RESPONSE_DTO.getDiaryId()))
                .andExpect(jsonPath("$.data[0].title").value(MOCK_MY_DIARY_RESPONSE_DTO.getTitle()))
                .andExpect(jsonPath("$.data[0].isShared").value(MOCK_MY_DIARY_RESPONSE_DTO.getIsShared()))
                .andExpect(jsonPath("$.data[0].thumbnail").value(MOCK_MY_DIARY_RESPONSE_DTO.getThumbnail()))
                .andExpect(jsonPath("$.success").value(true));











    }

}