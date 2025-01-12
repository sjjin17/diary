package diary.global.config.jwt;

import diary.domain.user.domain.User;
import diary.global.exception.CustomException;
import diary.global.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static diary.global.exception.ErrorCode.EXPIRED_ACCESS_TOKEN;
import static diary.global.exception.ErrorCode.INVALID_ACCESS_TOKEN_VALUE;

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


    // subject에 id넣기
    public String generateAccessToken(User user) {
        Date now = new Date();
        final Date expiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .subject(String.valueOf(user.getUserId()))
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

    public Long validateToken(String token) {
        try {
            return Long.parseLong(parseToken(token).getSubject());
        } catch (ExpiredJwtException e) {
            throw new TokenException(EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw new TokenException(INVALID_ACCESS_TOKEN_VALUE);
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(this.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }





}
