package diary.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import diary.domain.auth.dto.SocialUserInfo;
import diary.domain.auth.dto.response.TokenResponseDto;
import diary.domain.auth.strategy.LoginStrategy;
import diary.domain.user.domain.SocialType;
import diary.domain.user.domain.User;
import diary.domain.user.repository.UserRepository;
import diary.domain.user.service.UserService;
import diary.global.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final Map<String, LoginStrategy> loginStrategyMap;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public TokenResponseDto login(String code, String provider) throws JsonProcessingException {
        LoginStrategy loginStrategy = loginStrategyMap.get(provider);
        SocialUserInfo socialUserInfo = loginStrategy.login(code, SocialType.valueOf(provider));
        String refreshToken = jwtProvider.generateRefreshToken();
        User user = userRepository.findBySocialIdAndSocialType(socialUserInfo.socialId(), socialUserInfo.socialType()).orElse(null);
        if (user == null) {
            user = userService.join(socialUserInfo, refreshToken);
        }
        return TokenResponseDto.builder()
                .accessToken(jwtProvider.generateAccessToken(user))
                .refreshToken(refreshToken)
                .build();
    }

}
