package diary.global.config.jwt;

import diary.domain.auth.dto.SocialUserInfo;
import diary.domain.user.domain.User;
import diary.domain.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private String secretKey = "000000000000000000000000myDiarySecretKey202412171648000000000000000";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 12;  // 12 시간
    private long ACCESS_TOKEN_EXPIRE_TIME;  // ? 이름 규칙 다시 찾아보기
    private long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 7;  // 7일 보통은 어떻게 하는지 모르곘음 찾아봐야 함

    private final UserRepository userRepository;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }


    public String generateAccessToken(User user) {
        Date now = new Date();
        final Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .claim("name", user.getUsername())
                .claim("email", user.getEmail())
                .claim("imageUrl", user.getImageUrl())
                .claim("type", "ACCESS_TOKEN")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();

    }

    public String generateRefreshToken() {
        Date now = new Date();
        final Date expiration = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .claim("type", "REFRESH_TOKEN")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();

    }

}
