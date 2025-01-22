package todaktodak.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import todaktodak.domain.auth.dto.SocialUserInfo;
import todaktodak.domain.auth.dto.Token;
import todaktodak.domain.auth.strategy.LoginStrategy;
import todaktodak.domain.user.domain.SocialType;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.repository.UserRepository;
import todaktodak.domain.user.service.UserService;
import todaktodak.global.config.jwt.JwtProvider;
import todaktodak.global.exception.CustomException;
import todaktodak.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final Map<String, LoginStrategy> loginStrategyMap;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Transactional
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
    @Transactional
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
