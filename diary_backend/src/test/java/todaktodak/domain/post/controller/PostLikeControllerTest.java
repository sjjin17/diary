package todaktodak.domain.post.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import todaktodak.domain.diary.repository.DiaryRepository;
import todaktodak.domain.post.service.PostLikeService;
import todaktodak.domain.post.service.PostService;
import todaktodak.domain.user.repository.UserRepository;
import todaktodak.global.WithMockCustomUser;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static todaktodak.domain.post.fixture.PostFixture.MOCK_POST_LIST_RESPONSE;
import static todaktodak.domain.post.fixture.PostLikeFixture.MOCK_POST_LIKE_RESPONSE;

@WebMvcTest(PostLikeController.class)
@AutoConfigureRestDocs
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class PostLikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PostLikeService postLikeService;

    private static final String ACCESS_TOKEN_PREFIX = "Bearer ";


    @Test
    @WithMockCustomUser
    void 좋아요_요청() throws Exception {
        // given
        Long userId = 1L;
        Long postId = 1L;

        given(postLikeService.likePost(anyLong(), anyLong())).willReturn(MOCK_POST_LIKE_RESPONSE);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/posts/{postId}/like", postId)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "accessToken")
                        .with(csrf())

        );

        // then
        resultActions.andDo(document("postLike/like", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.isLike").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.isLike").value(MOCK_POST_LIKE_RESPONSE.getIsLike()))
                .andExpect(jsonPath("$.data.likeCount").value(MOCK_POST_LIKE_RESPONSE.getLikeCount()))
                .andExpect(jsonPath("$.success").value(true));
    }




}
