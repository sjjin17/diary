package todaktodak.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.diary.repository.DiaryRepository;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.dto.request.PostCreateRequestDto;
import todaktodak.domain.post.repository.PostRepository;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.repository.UserRepository;
import todaktodak.global.exception.CustomException;
import todaktodak.global.exception.ErrorCode;

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


}
