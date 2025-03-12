package todaktodak.domain.post.fixture;

import todaktodak.domain.post.dto.response.PostLikeResponseDto;
import todaktodak.domain.post.dto.response.PostListResponseDto;
import todaktodak.domain.post.repository.PostLikeRepository;

public class PostLikeFixture {

    public static final PostLikeResponseDto MOCK_POST_LIKE_RESPONSE =
            PostLikeResponseDto.builder()
                    .isLike(true)
                    .likeCount(1L)
                    .build();

    public static final PostLikeResponseDto MOCK_POST_UNLIKE_RESPONSE =
            PostLikeResponseDto.builder()
                    .isLike(false)
                    .likeCount(0L)
                    .build();
}
