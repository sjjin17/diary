package todaktodak.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.domain.PostImage;
import todaktodak.domain.post.dto.PostAndPostLikeDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostDetailResponseDto {
    private Long postId;
    private String writtenDate;
    private String title;
    private String content;
    private String weather;
    private String emotion;
    private Boolean isPublished;
    private Long likeCount;

    @Builder
    public PostDetailResponseDto(Long postId, String writtenDate, String title, String content, String weather, String emotion, Boolean isPublished, Long likeCount) {
        this.postId = postId;
        this.writtenDate = writtenDate;
        this.title = title;
        this.content = content;
        this.weather = weather;
        this.emotion = emotion;
        this.isPublished = isPublished;
        this.likeCount = likeCount;
    }

    public static PostDetailResponseDto of(PostAndPostLikeDto postAndPostLike) {
        return PostDetailResponseDto.builder()
                .postId(postAndPostLike.getPostId())
                .writtenDate(postAndPostLike.getWrittenDate().toString())
                .title(postAndPostLike.getTitle())
                .content(postAndPostLike.getContent())
                .weather(postAndPostLike.getWeather().name())
                .emotion(postAndPostLike.getEmotion().name())
                .isPublished(postAndPostLike.getIsPublished())
                .likeCount(postAndPostLike.getLikeCount())
                .build();


    }

}
