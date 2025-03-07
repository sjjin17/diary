package todaktodak.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.domain.PostImage;

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
    private int likeCount;
    private List<String> imageList;

    @Builder
    public PostDetailResponseDto(Long postId, String writtenDate, String title, String content, String weather, String emotion, Boolean isPublished, int likeCount, List<String> imageList) {
        this.postId = postId;
        this.writtenDate = writtenDate;
        this.title = title;
        this.content = content;
        this.weather = weather;
        this.emotion = emotion;
        this.isPublished = isPublished;
        this.likeCount = likeCount;
        this.imageList = imageList;
    }

    public static PostDetailResponseDto of(Post post) {
        return PostDetailResponseDto.builder()
                .postId(post.getId())
                .writtenDate(post.getWrittenDate().toString())
                .title(post.getTitle())
                .content(post.getContent())
                .weather(post.getWeather().name())
                .emotion(post.getEmotion().name())
                .isPublished(post.getIsPublished())
                .likeCount(post.getLikeCount())
                .imageList(post.getPostImageList().stream().map(PostImage::getImageUrl).collect(Collectors.toList()))
                .build();


    }

}
