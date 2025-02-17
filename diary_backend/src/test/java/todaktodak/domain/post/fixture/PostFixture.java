package todaktodak.domain.post.fixture;

import todaktodak.domain.post.dto.request.PostCreateRequestDto;

public class PostFixture {

    public static final PostCreateRequestDto MOCK_POST_CREATE_REQUEST = PostCreateRequestDto.builder().writtenDate("2025-02-11").weather("SUNNY").emotion("HAPPY").title("제목").content("내용").build();

}
