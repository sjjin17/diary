package todaktodak.domain.user.service;

import todaktodak.domain.auth.dto.SocialUserInfo;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.diary.repository.DiaryRepository;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.dto.response.MyDiaryResponseDto;
import todaktodak.domain.user.repository.MemberRepository;
import todaktodak.domain.user.repository.UserRepository;
import todaktodak.global.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;



    public User join(SocialUserInfo socialUserInfo, String refreshToken) {
        User user = User.builder()
                .socialId(socialUserInfo.socialId())
                .email(socialUserInfo.email())
                .username(socialUserInfo.name())
                .imageUrl(socialUserInfo.imageUrl())
                .socialType(socialUserInfo.socialType())
                .refreshToken(refreshToken)
                .build();
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<MyDiaryResponseDto> getAllDiary(long userId) {
        List<Diary> diaries = diaryRepository.findALlByMemberList_User_Id(userId);
        return diaries.stream().map(MyDiaryResponseDto::from).collect(Collectors.toList());
    }

}
