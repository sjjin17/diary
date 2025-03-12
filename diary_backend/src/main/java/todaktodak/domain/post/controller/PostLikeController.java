package todaktodak.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todaktodak.domain.post.service.PostLikeService;
import todaktodak.global.api.BasicResponse;
import todaktodak.global.api.CommonResponse;
import todaktodak.global.common.LoginUser;

@RestController
@RequestMapping("/posts/{postId}/like")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;


    @PostMapping
    public ResponseEntity<? extends BasicResponse> likePost(@LoginUser Long userId, @PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(postLikeService.likePost(userId, postId)));
    }

}