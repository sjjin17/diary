package todaktodak.domain.post.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.diary.fixture.DiaryFixture;
import todaktodak.domain.diary.repository.DiaryRepository;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.dto.request.PostUpdateRequestDto;
import todaktodak.domain.post.fixture.PostFixture;
import todaktodak.domain.post.repository.PostRepository;
import todaktodak.domain.user.domain.SocialType;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.fixture.UserFixture;
import todaktodak.domain.user.repository.UserRepository;
import todaktodak.global.WithMockCustomUser;
import todaktodak.global.exception.CustomException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.spy;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    DiaryRepository diaryRepository;

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    private static User user;
    private static Diary diary;
    private static Post post;
    @BeforeEach
    public void setUp() {
        user = spy(UserFixture.createUser(SocialType.GOOGLE));
        diary = spy(DiaryFixture.createDiary("title", true));
        post = spy(PostFixture.createPost("2025-02-15", true, user, diary));


    }

    @Test
    void 유저가_작성한_일기_수정() {
        // given
        Long userId = 1L;
        Long diaryId = 1L;
        Long postId = 1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(post.getId()).willReturn(postId);
        PostUpdateRequestDto mockPostUpdateRequest = PostFixture.MOCK_POST_UPDATE_REQUEST;

        // when
        Long updatePostId = postService.updatePost(userId, diaryId, postId, mockPostUpdateRequest);

        // then
        assertThat(updatePostId).isEqualTo(diaryId);
        assertThat(post.getContent()).isEqualTo(mockPostUpdateRequest.getContent());


    }

    @Test
    void 유저가_작성하지_않은_일기_수정시_예외_발생() {
        // given
        Long userId = 2L;
        Long diaryId = 1L;
        Long postId = 1L;

        User otherUser = UserFixture.createUser(SocialType.KAKAO);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(otherUser));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        PostUpdateRequestDto mockPostUpdateRequest = PostFixture.MOCK_POST_UPDATE_REQUEST;

        // when & then
        assertThrows(CustomException.class, () -> postService.updatePost(userId, diaryId, postId, mockPostUpdateRequest), "No access");

    }




}