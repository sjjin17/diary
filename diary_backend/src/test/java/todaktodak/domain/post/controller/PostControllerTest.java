package todaktodak.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PostUpdate;
import org.apache.http.HttpHeaders;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import todaktodak.domain.auth.controller.AuthController;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.diary.fixture.DiaryFixture;
import todaktodak.domain.diary.repository.DiaryRepository;
import todaktodak.domain.post.dto.request.PostCreateRequestDto;
import todaktodak.domain.post.dto.request.PostUpdateRequestDto;
import todaktodak.domain.post.fixture.PostFixture;
import todaktodak.domain.post.service.PostService;
import todaktodak.domain.user.domain.SocialType;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.fixture.UserFixture;
import todaktodak.domain.user.repository.UserRepository;
import todaktodak.global.WithMockCustomUser;

import java.util.List;
import java.util.Optional;


import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static todaktodak.domain.diary.fixture.DiaryFixture.MOCK_MY_DIARY_RESPONSE_DTO;
import static todaktodak.domain.post.fixture.PostFixture.*;

@WebMvcTest(PostController.class)
@AutoConfigureRestDocs
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PostService postService;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    DiaryRepository diaryRepository;

    private static final String ACCESS_TOKEN_PREFIX = "Bearer ";
    private User user;
    private Diary diary;


    @BeforeEach
    public void setUp() {
        user = UserFixture.createUser(SocialType.GOOGLE);
        diary = DiaryFixture.createDiary("title", true);

    }

    @Test
    @WithMockCustomUser
    void 일기_작성() throws Exception {
        // given
        Long userId = 1L;
        Long diaryId = 1L;
        PostCreateRequestDto postCreateRequest = MOCK_POST_CREATE_REQUEST;
        given(postService.createPost(anyLong(), anyLong(), any(PostCreateRequestDto.class))).willReturn(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(postCreateRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/diaries/{diaryId}/posts", diaryId)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf()));


        // then
        resultActions.andDo(document("post/createPost",  preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("Post Id"),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부")
                )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data").value(1L))
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    @WithMockCustomUser
    void 특정_월의_일기_조회() throws Exception {
        // given
        Long userId = 1L;
        Long diaryId = 1L;
        int year = 2025;
        int month = 2;

        given(postService.getPostsByMonth(anyLong(), anyLong(), anyInt(), anyInt())).willReturn(List.of(MOCK_POST_LIST_RESPONSE));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/diaries/{diaryId}/posts", diaryId)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month))
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "accessToken"));

        // then
        resultActions.andDo(document("post/getPostListByMonth", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                                fieldWithPath("data[].postId").type(JsonFieldType.NUMBER).description("일기 Id"),
                                fieldWithPath("data[].writtenDate").type(JsonFieldType.STRING).description("일기 작성일"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("일기 제목"),
                                fieldWithPath("data[].emotion").type(JsonFieldType.STRING).description("그날의 감정"),
                                fieldWithPath("data[].isPublished").type(JsonFieldType.BOOLEAN).description("최종 저장 여부"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data[0].postId").value(MOCK_POST_LIST_RESPONSE.getPostId()))
                .andExpect(jsonPath("$.data[0].writtenDate").value(MOCK_POST_LIST_RESPONSE.getWrittenDate()))
                .andExpect(jsonPath("$.data[0].title").value(MOCK_POST_LIST_RESPONSE.getTitle()))
                .andExpect(jsonPath("$.data[0].emotion").value(MOCK_POST_LIST_RESPONSE.getEmotion()))
                .andExpect(jsonPath("$.data[0].isPublished").value(MOCK_POST_LIST_RESPONSE.getIsPublished()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockCustomUser
    void 일기_수정_성공() throws Exception {
        // given
        Long diaryId = 1L;
        Long postId = 1L;
        PostUpdateRequestDto postUpdateRequest = MOCK_POST_UPDATE_REQUEST;
        given(postService.updatePost(anyLong(), anyLong(), anyLong(), any(PostUpdateRequestDto.class))).willReturn(postId);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(postUpdateRequest);


        // when
        ResultActions resultActions = mockMvc.perform(
                put("/diaries/{diaryId}/posts/{postId}", diaryId, postId)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf()));


        // then
        resultActions.andDo(document("post/updatePost",  preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("Post Id"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data").value(postId))
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    @WithMockCustomUser
    void 일기_삭제() throws Exception {
        // given
        Long diaryId = 1L;
        Long postId = 1L;
        given(postService.deletePost(anyLong(), anyLong(), anyLong())).willReturn(postId);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/diaries/{diaryId}/posts/{postId}", diaryId, postId)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()));



        // then
        resultActions.andDo(document("post/deletePost",  preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("Post Id"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data").value(postId))
                .andExpect(jsonPath("$.success").value(true));

    }

}