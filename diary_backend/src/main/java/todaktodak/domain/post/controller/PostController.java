package todaktodak.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todaktodak.domain.post.dto.request.PostCreateRequestDto;
import todaktodak.domain.post.service.PostService;
import todaktodak.global.api.BasicResponse;
import todaktodak.global.api.CommonResponse;
import todaktodak.global.common.BaseEntity;
import todaktodak.global.common.LoginUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries/{diaryId}/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<? extends BasicResponse> createPost(@LoginUser Long userId, @PathVariable Long diaryId, @RequestBody PostCreateRequestDto postCreateRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(postService.createPost(userId, diaryId, postCreateRequestDto)));
    }

}
