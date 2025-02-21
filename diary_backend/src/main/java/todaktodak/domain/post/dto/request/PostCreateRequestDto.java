package todaktodak.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.post.domain.Emotion;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.domain.PostImage;
import todaktodak.domain.post.domain.Weather;
import todaktodak.domain.user.domain.User;

import java.time.LocalDate;
import java.util.List;

@Getter
public class PostCreateRequestDto {
    private String writtenDate;
    private String weather;
    private String emotion;
    private String title;
    private String content;

    @Builder
    public PostCreateRequestDto(String writtenDate, String weather, String emotion, String title, String content) {
        this.writtenDate = writtenDate;
        this.weather = weather;
        this.emotion = emotion;
        this.title = title;
        this.content = content;
    }
    public Post toEntity(User user, Diary diary) {
        return Post.builder()
                .writtenDate(LocalDate.parse(writtenDate))
                .title(title)
                .content(content)
                .weather(Weather.valueOf(weather))
                .emotion(Emotion.valueOf(emotion))
                .isPublished(true)
                .user(user)
                .diary(diary)
                .build();


    }

}
