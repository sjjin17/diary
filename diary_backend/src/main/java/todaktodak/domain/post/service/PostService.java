package todaktodak.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.diary.repository.DiaryRepository;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.dto.request.PostCreateRequestDto;
import todaktodak.domain.post.dto.request.PostUpdateRequestDto;
import todaktodak.domain.post.dto.response.PostListResponseDto;
import todaktodak.domain.post.repository.PostRepository;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.repository.UserRepository;
import todaktodak.global.exception.CustomException;
import todaktodak.global.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final PostRepository postRepository;
    @Transactional
    public Long createPost(Long userId, Long diaryId, PostCreateRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY));
        Post post = postRepository.save(requestDto.toEntity(user, diary));
        return post.getId();
    }


    public List<PostListResponseDto> getPostsByMonth(Long userId, Long diaryId, int year, int month) {
        List<Post> postList = postRepository.findPostsByDiaryIdAndMonth(userId, diaryId, year, month);
        return postList.stream().map(post -> PostListResponseDto.of(post)).collect(Collectors.toList());
    }

    @Transactional
    public Long updatePost(Long userId, Long diaryId, Long postId, PostUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        validatePostAuthor(user, post);
        post.updatePost(requestDto.getWeather(), requestDto.getEmotion(), requestDto.getTitle(), requestDto.getContent());
        return post.getId();
    }

    @Transactional
    public Long deletePost(Long userId, Long diaryId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        validatePostAuthor(user, post);
        postRepository.deleteById(postId);
        return post.getId();
    }

    private void validatePostAuthor(User user, Post post) {
        if (!post.isAuthor(user)) {
            throw new CustomException(ErrorCode.NO_ACCESS);
        }
    }

}
