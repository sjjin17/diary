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
import todaktodak.domain.post.dto.PostAndPostLikeDto;
import todaktodak.domain.post.dto.PostAndUserDto;
import todaktodak.domain.post.fixture.PostFixture;
import todaktodak.domain.post.fixture.PostLikeFixture;
import todaktodak.domain.user.domain.SocialType;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.fixture.UserFixture;
import todaktodak.domain.user.repository.MemberRepository;
import todaktodak.domain.user.repository.UserRepository;
import todaktodak.global.exception.CustomException;
import todaktodak.global.exception.ErrorCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    DiaryRepository diaryRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("userA의 입장에서 특정 월에 작성된 일기를 반환한다.")
    void 특정_월에_작성된_일기_조회() {
        // given
        User userA = UserFixture.createUser(SocialType.GOOGLE);
        User userB = UserFixture.createUser(SocialType.NAVER);

        Diary publicDiary = DiaryFixture.createDiary("title", true);

        userRepository.saveAll(List.of(userA, userB));
        diaryRepository.save(publicDiary);


        Post tempPostByUserA = PostFixture.createPost("2025-02-15", false, userA, publicDiary);
        Post tempPostByUserB = PostFixture.createPost("2025-02-16", false, userB, publicDiary);
        Post finalPostByUserA = PostFixture.createPost("2025-02-17", true, userA, publicDiary);
        Post finalPostByUserB = PostFixture.createPost("2025-02-18", true, userB, publicDiary);
        Post postWrittenInJanuary = PostFixture.createPost("2025-01-01", true, userA, publicDiary);

        postRepository.saveAll(List.of(tempPostByUserA, tempPostByUserB, finalPostByUserA, finalPostByUserB, postWrittenInJanuary));

        LocalDate startDate = LocalDate.of(2025, 02, 1);
        LocalDate endDate = LocalDate.of(2025, 03, 1);


        // when
        List<PostAndUserDto> postList = postRepository.findPostsByDiaryIdAndMonth(userA.getId(), publicDiary.getId(), startDate, endDate);

        // then
        assertThat(postList.size()).isEqualTo(3);

    }

    @Test
    @DisplayName("userA의 입장에서 특정 날짜에 작성된 일기를 조회한다.")
    void 특정_날짜에_작성된_일기_조회() {
        // given
        User userA = UserFixture.createUser(SocialType.GOOGLE);
        User userB = UserFixture.createUser(SocialType.NAVER);

        Diary publicDiary = DiaryFixture.createDiary("title", true);

        userRepository.saveAll(List.of(userA, userB));
        diaryRepository.save(publicDiary);

        Post tempPostByUserA = PostFixture.createPost("2025-02-15", false, userA, publicDiary);
        Post tempPostByUserB = PostFixture.createPost("2025-02-16", false, userB, publicDiary);
        Post finalPostByUserA = PostFixture.createPost("2025-02-16", true, userA, publicDiary);
        Post finalPostByUserB = PostFixture.createPost("2025-02-18", true, userB, publicDiary);

        postRepository.saveAll(List.of(tempPostByUserA, tempPostByUserB, finalPostByUserA, finalPostByUserB));

        // when
        List<PostAndUserDto> postList = postRepository.findPostsByDiaryIdAndWrittenDate(userB.getId(), publicDiary.getId(), LocalDate.parse("2025-02-16"));

        // then
        assertThat(postList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("일기 상세 조회")
    void 일기_상세_조회() throws Exception {
        // given
        User userA = UserFixture.createUser(SocialType.GOOGLE);
        User userB = UserFixture.createUser(SocialType.NAVER);

        Diary publicDiary = DiaryFixture.createDiary("title", true);

        userRepository.saveAll(List.of(userA, userB));
        diaryRepository.save(publicDiary);

        Post post = PostFixture.createPost("2025-02-16", true, userA, publicDiary);
        PostLike postLikeA = PostLikeFixture.createPostLike(post, userA);
        PostLike postLikeB = PostLikeFixture.createPostLike(post, userB);

        postRepository.save(post);
        postLikeRepository.saveAll(List.of(postLikeA, postLikeB));

        // when
        PostAndPostLikeDto postAndLike = postRepository.findPostAndLikeById(post.getId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // then
        assertThat(postAndLike.getPostId()).isEqualTo(post.getId());
        assertThat(postAndLike.getWrittenDate()).isEqualTo((post.getWrittenDate()));
        assertThat(postAndLike.getTitle()).isEqualTo(post.getTitle());
        assertThat(postAndLike.getContent()).isEqualTo(post.getContent());
        assertThat(postAndLike.getWeather()).isEqualTo(post.getWeather());
        assertThat(postAndLike.getEmotion()).isEqualTo(post.getEmotion());
        assertThat(postAndLike.getIsPublished()).isEqualTo(post.getIsPublished());
        assertThat(postAndLike.getLikeCount()).isEqualTo(2);



    }
}