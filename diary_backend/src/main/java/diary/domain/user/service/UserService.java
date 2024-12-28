package diary.domain.user.service;

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

    public User join(String socialId, String email, String username, String imageUrl, SocialType socialType) {
        User user = User.builder()
                .socialId(socialId)
                .email(email)
                .username(username)
                .imageUrl(imageUrl)
                .socialType(socialType)
                .refreshToken(jwtProvider.generateRefreshToken())
                .build();
        userRepository.save(user);
        return user;
    }

}
