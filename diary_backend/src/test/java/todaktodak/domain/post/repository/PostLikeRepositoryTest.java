package todaktodak.domain.post.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.diary.fixture.DiaryFixture;
import todaktodak.domain.diary.repository.DiaryRepository;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.domain.PostLike;
import todaktodak.domain.post.fixture.PostFixture;
import todaktodak.domain.user.domain.SocialType;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.fixture.UserFixture;
import todaktodak.domain.user.repository.UserRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostLikeRepositoryTest {

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    DiaryRepository diaryRepository;

    @Autowired
    UserRepository userRepository;

    private static User user;
    private static Diary diary;

    private static Post post;

    @BeforeEach
    public void setUp() {
        user = UserFixture.createUser(SocialType.GOOGLE);
        diary = DiaryFixture.createDiary("title", true);
        post = PostFixture.createPost("2025-02-15", true, user, diary);

        userRepository.save(user);
        diaryRepository.save(diary);
        postRepository.save(post);

    }

    @Test
    @DisplayName("userA가 본인의 좋아요 취소하기")
    void 좋아요_취소() {
        // 잘 취소하고 총 좋아요 수도 잘 변화되었는지 확인해야 한다. 
        // given
        PostLike postLike = PostLike.builder()
                .user(user)
                .post(post)
                .build();
        PostLike like = postLikeRepository.save(postLike);
        Long postLikeId = like.getPostLikeId();

        // when
        postLikeRepository.deleteByPostIdAndUserId(post.getId(), user.getId());


        // then
        assertThat(postLikeRepository.findById(postLikeId)).isEmpty();

    }
}