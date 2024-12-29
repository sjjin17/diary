package diary.global.config.jwt;

import diary.domain.auth.dto.SocialUserInfo;
import diary.domain.user.domain.User;
import diary.domain.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.expire-time.access-token}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.expire-time.refresh-token}")
    private long REFRESH_TOKEN_EXPIRE_TIME;


    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }


    public String generateAccessToken(User user) {
        Date now = new Date();
        final Date expiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
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
