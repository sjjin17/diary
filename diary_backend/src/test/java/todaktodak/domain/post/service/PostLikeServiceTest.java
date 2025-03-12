package todaktodak.domain.post.service;

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
import todaktodak.domain.post.dto.response.PostLikeResponseDto;
import todaktodak.domain.post.fixture.PostFixture;
import todaktodak.domain.post.repository.PostLikeRepository;
import todaktodak.domain.post.repository.PostRepository;
import todaktodak.domain.user.domain.SocialType;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.fixture.UserFixture;
import todaktodak.domain.user.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostLikeServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    PostRepository postRepository;

    @Mock
    PostLikeRepository postLikeRepository;

    @InjectMocks
    PostLikeService postLikeService;

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
    void 좋아요_성공() throws Exception {
        // given
        given(post.getId()).willReturn(1L);
        given(user.getId()).willReturn(1L);
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(postLikeRepository.existsByPostIdAndUserId(post.getId(), user.getId())).willReturn(false);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        // when
        PostLikeResponseDto postLikeResponseDto = postLikeService.likePost(user.getId(), post.getId());

        // then
        assertThat(postLikeResponseDto.getIsLike()).isEqualTo(true);
    }

    @Test
    void 좋아요_취소() throws Exception {
        // given
        given(post.getId()).willReturn(1L);
        given(user.getId()).willReturn(1L);
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(postLikeRepository.existsByPostIdAndUserId(post.getId(), user.getId())).willReturn(true);

        // when
        PostLikeResponseDto postLikeResponseDto = postLikeService.likePost(user.getId(), post.getId());

        // then
        assertThat(postLikeResponseDto.getIsLike()).isEqualTo(false);
    }
}
