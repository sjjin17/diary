package todaktodak.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import todaktodak.domain.post.domain.Emotion;
import todaktodak.domain.post.domain.Post;

import java.time.LocalDate;

@Getter
public class PostListResponseDto {
    private Long postId;
    private String writtenDate;
    private String title;
    private String emotion;
    private Boolean isPublished;


    @Builder
    public PostListResponseDto(Long postId, LocalDate writtenDate, String title, Emotion emotion, Boolean isPublished) {
        this.postId = postId;
        this.writtenDate = writtenDate.toString();
        this.title = title;
        this.emotion = emotion.name();
        this.isPublished = isPublished;
    }


    public static PostListResponseDto of(Post post) {
        return PostListResponseDto.builder()
                .postId(post.getId())
                .writtenDate(post.getWrittenDate())
                .title(post.getTitle())
                .emotion(post.getEmotion())
                .isPublished(post.getIsPublished())
                .build();
    }

}
