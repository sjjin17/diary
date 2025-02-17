package todaktodak.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import todaktodak.domain.post.fixture.PostFixture;
import todaktodak.domain.post.service.PostService;
import todaktodak.domain.user.domain.SocialType;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.fixture.UserFixture;
import todaktodak.domain.user.repository.UserRepository;
import todaktodak.global.WithMockCustomUser;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        final PostCreateRequestDto postCreateRequest = PostFixture.MOCK_POST_CREATE_REQUEST;
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


}