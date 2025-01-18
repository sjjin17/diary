package diary.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import diary.domain.auth.dto.SocialUserInfo;
import diary.domain.auth.dto.Token;
import diary.domain.auth.strategy.LoginStrategy;
import diary.domain.user.domain.SocialType;
import diary.domain.user.domain.User;
import diary.domain.user.repository.UserRepository;
import diary.domain.user.service.UserService;
import diary.global.config.jwt.JwtProvider;
import diary.global.exception.CustomException;
import diary.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final Map<String, LoginStrategy> loginStrategyMap;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public Token login(String code, String provider) throws JsonProcessingException {
        LoginStrategy loginStrategy = loginStrategyMap.get(provider);
        SocialUserInfo socialUserInfo = loginStrategy.login(code, SocialType.valueOf(provider));
        String refreshToken = jwtProvider.generateRefreshToken();
        User user = userRepository.findBySocialIdAndSocialType(socialUserInfo.socialId(), socialUserInfo.socialType()).orElse(null);
        if (user == null) {
            user = userService.join(socialUserInfo, refreshToken);
        }
        return Token.builder()
                .accessToken(jwtProvider.generateAccessToken(user))
                .refreshToken(refreshToken)
                .build();
    }

    public Token reissue(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        String accessToken = jwtProvider.generateAccessToken(user);
        if (jwtProvider.checkRefreshTokenExpiration(refreshToken) < (1000 * 60 * 60 * 24 * 3)) {
            refreshToken = jwtProvider.generateRefreshToken();
            user.updateRefreshToken(refreshToken);
        }
        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

}
