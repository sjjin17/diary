package todaktodak.domain.post.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import todaktodak.domain.post.dto.request.PostCreateRequestDto;
import todaktodak.domain.post.dto.request.PostUpdateRequestDto;
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

    @GetMapping("{year}/{month}")
    public ResponseEntity<? extends BasicResponse> getPostsByMonth(@LoginUser Long userId, @PathVariable Long diaryId, @PathVariable int year, @PathVariable @Min(1) @Max(12) int month) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.getPostsByMonth(userId, diaryId, year, month)));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<? extends BasicResponse> updatePost(@LoginUser Long userId, @PathVariable Long diaryId, @PathVariable Long postId, @RequestBody PostUpdateRequestDto postUpdateRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.updatePost(userId, diaryId, postId, postUpdateRequestDto)));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<? extends BasicResponse> deletePost(@LoginUser Long userId, @PathVariable Long diaryId, @PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postService.deletePost(userId, diaryId, postId)));
    }

}
