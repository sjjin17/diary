package todaktodak.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.domain.PostLike;
import todaktodak.domain.post.dto.response.PostLikeResponseDto;
import todaktodak.domain.post.repository.PostLikeRepository;
import todaktodak.domain.post.repository.PostRepository;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.repository.UserRepository;
import todaktodak.global.exception.CustomException;
import todaktodak.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;


    @Transactional
    public PostLikeResponseDto likePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        Boolean isLike;

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            postLikeRepository.deleteByPostIdAndUserId(postId, userId);
            isLike = false;
        } else {
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
            PostLike postLike = PostLike.builder()
                    .post(post)
                    .user(user)
                    .build();
            postLikeRepository.save(postLike);
            isLike = true;

        }

        return PostLikeResponseDto.builder().isLike(isLike)
                .likeCount(postLikeRepository.countByPostId(postId))
                        .build();


    }

}