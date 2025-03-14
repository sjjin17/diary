package todaktodak.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import todaktodak.domain.post.domain.Emotion;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PostAndUserDto {
    private Long postId;
    private String username;
    private LocalDate writtenDate;
    private String title;
    private Emotion emotion;
    private Boolean isPublished;
}
