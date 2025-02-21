package todaktodak.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.user.domain.User;

@Getter
public class PostUpdateRequestDto {
    private String weather;
    private String emotion;
    private String title;
    private String content;

    @Builder
    public PostUpdateRequestDto(String weather, String emotion, String title, String content) {
        this.weather = weather;
        this.emotion = emotion;
        this.title = title;
        this.content = content;
    }
}
