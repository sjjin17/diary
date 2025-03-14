package todaktodak.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import todaktodak.domain.post.domain.Emotion;
import todaktodak.domain.post.domain.Weather;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostAndPostLikeDto {
    private Long postId;
    private LocalDate writtenDate;
    private String title;
    private String content;
    private Weather weather;
    private Emotion emotion;
    private Boolean isPublished;
    private Long likeCount;


}
