package todaktodak.domain.post.fixture;

import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.post.domain.Emotion;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.domain.Weather;
import todaktodak.domain.post.dto.request.PostCreateRequestDto;
import todaktodak.domain.post.dto.request.PostUpdateRequestDto;
import todaktodak.domain.post.dto.response.PostDetailResponseDto;
import todaktodak.domain.post.dto.response.PostListResponseDto;
import todaktodak.domain.user.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class PostFixture {
    private static final String WRITTEN_DATE = "2025-02-15";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String UPDATED_CONTENT = "수정된 content";
    private static final Weather WEATHER = Weather.SNOWY;
    private static final Emotion EMOTION = Emotion.HAPPY;



    public static Post createPost(String writtenDate, Boolean isPublished, User user, Diary diary) {
        return Post.builder()
                .writtenDate(LocalDate.parse(writtenDate))
                .title(TITLE)
                .content(CONTENT)
                .weather(WEATHER)
                .emotion(EMOTION)
                .isPublished(isPublished)
                .user(user)
                .diary(diary)
                .build();

    }


    public static final PostCreateRequestDto MOCK_POST_CREATE_REQUEST =
            PostCreateRequestDto.builder()
                    .writtenDate("2025-02-11")
                    .weather("SUNNY")
                    .emotion("HAPPY")
                    .title("제목")
                    .content("내용")
                    .build();

    public static final PostListResponseDto MOCK_POST_LIST_RESPONSE =
            PostListResponseDto.builder()
                    .postId(1L)
                    .username("userA")
                    .writtenDate(WRITTEN_DATE)
                    .title(TITLE)
                    .emotion(EMOTION)
                    .isPublished(true)
                    .build();

    public static final PostUpdateRequestDto MOCK_POST_UPDATE_REQUEST =
            PostUpdateRequestDto.builder()
                    .weather(WEATHER.name())
                    .emotion(EMOTION.name())
                    .title(TITLE)
                    .content(UPDATED_CONTENT)
                    .build();

    public static final PostDetailResponseDto MOCK_POST_DETAIL_RESPONSE =
            PostDetailResponseDto.builder()
                    .postId(1L)
                    .writtenDate(WRITTEN_DATE)
                    .title(TITLE)
                    .content(CONTENT)
                    .weather(WEATHER.name())
                    .emotion(EMOTION.name())
                    .isPublished(true)
                    .likeCount(0)
                    .imageList(new ArrayList<>())
                    .build();


}
