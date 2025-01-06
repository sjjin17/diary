package diary.domain.user.service;

import diary.domain.auth.dto.SocialUserInfo;
import diary.domain.user.domain.SocialType;
import diary.domain.user.domain.User;
import diary.domain.user.repository.UserRepository;
import diary.global.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

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

}
