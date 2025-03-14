package todaktodak.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostLikeResponseDto {
    private Boolean isLike;
    private Long likeCount;

    @Builder
    public PostLikeResponseDto(Boolean isLike, Long likeCount) {
        this.isLike = isLike;
        this.likeCount = likeCount;
    }
}
