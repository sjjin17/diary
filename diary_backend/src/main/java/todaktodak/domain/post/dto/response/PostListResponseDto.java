package todaktodak.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import todaktodak.domain.post.domain.Emotion;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.dto.PostAndUserDto;

import java.time.LocalDate;

@Getter
public class PostListResponseDto {
    private Long postId;
    private String username;
    private String writtenDate;
    private String title;
    private String emotion;
    private Boolean isPublished;


    @Builder
    public PostListResponseDto(Long postId, String username, String writtenDate, String title, Emotion emotion, Boolean isPublished) {
        this.postId = postId;
        this.username = username;
        this.writtenDate = writtenDate;
        this.title = title;
        this.emotion = emotion.name();
        this.isPublished = isPublished;
    }


    public static PostListResponseDto of(Post post) {
        return PostListResponseDto.builder()
                .postId(post.getId())
                .username(post.getUser().getUsername())
                .writtenDate(post.getWrittenDate().toString())
                .title(post.getTitle())
                .emotion(post.getEmotion())
                .isPublished(post.getIsPublished())
                .build();
    }

    public static PostListResponseDto of(PostAndUserDto postAndUser) {
        return PostListResponseDto.builder()
                .postId(postAndUser.getPostId())
                .username(postAndUser.getUsername())
                .writtenDate(postAndUser.getWrittenDate().toString())
                .title(postAndUser.getTitle())
                .emotion(postAndUser.getEmotion())
                .isPublished(postAndUser.getIsPublished())
                .build();
    }

}
