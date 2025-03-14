package todaktodak.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.diary.repository.DiaryRepository;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.dto.PostAndPostLikeDto;
import todaktodak.domain.post.dto.PostAndUserDto;
import todaktodak.domain.post.dto.request.PostCreateRequestDto;
import todaktodak.domain.post.dto.request.PostUpdateRequestDto;
import todaktodak.domain.post.dto.response.PostDetailResponseDto;
import todaktodak.domain.post.dto.response.PostListResponseDto;
import todaktodak.domain.post.repository.PostRepository;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.repository.UserRepository;
import todaktodak.global.exception.CustomException;
import todaktodak.global.exception.ErrorCode;

import java.time.LocalDate;
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
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month+1, 1);
        List<PostAndUserDto> postList = postRepository.findPostsByDiaryIdAndMonth(userId, diaryId, startDate, endDate);
        return postList.stream().map(PostListResponseDto::of).collect(Collectors.toList());
    }

    public List<PostListResponseDto> getPostsByDate(Long userId, Long diaryId, LocalDate writtenDate) {
        List<PostAndUserDto> postList = postRepository.findPostsByDiaryIdAndWrittenDate(userId, diaryId, writtenDate);
        return postList.stream().map(PostListResponseDto::of).collect(Collectors.toList());
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

    public PostDetailResponseDto getPostDetail(Long postId) {
        PostAndPostLikeDto postAndPostLikeDto = postRepository.findPostAndLikeById(postId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        return PostDetailResponseDto.of(postAndPostLikeDto);
    }






}
