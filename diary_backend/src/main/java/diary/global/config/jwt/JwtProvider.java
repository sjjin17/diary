package diary.global.config.jwt;

import diary.domain.user.domain.User;
import diary.global.exception.CustomException;
import diary.global.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

import static diary.global.exception.ErrorCode.*;

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
        String encoded = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        return Keys.hmacShaKeyFor(encoded.getBytes());
    }



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

    public Long validateAccessToken(String token) {
        try {
            return Long.parseLong(parseToken(token).getSubject());
        } catch (ExpiredJwtException e) {
            throw new TokenException(EXPIRED_ACCESS_TOKEN);
        } catch (TokenException e) {
            throw new TokenException(INVALID_ACCESS_TOKEN_VALUE);
        }
    }



    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public long checkRefreshTokenExpiration(String refreshToken) {
        // getTime() 전의 반환값이 Date 객체이기 때문에 LocalDate로 현재 시간을 구해줄 필요는 없다.
        long expirationTime = parseToken(refreshToken).getExpiration().getTime();  // 30일의 밀리초를 반환하는게 아니라 refresh 토큰 발급일에서 30일을 더한 값을 밀리초로 반환
        long now = new Date().getTime();
        return expirationTime - now;
    }

    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            throw new CustomException(UNAUTHORIZED_REFRESH_TOKEN);
        }
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return validateRefreshToken(cookie.getValue());
            }
        }
        throw new CustomException(UNAUTHORIZED_REFRESH_TOKEN);
    }

    private String validateRefreshToken(String refreshToken) {
        try {
            parseToken(refreshToken);
            return refreshToken;
        } catch (ExpiredJwtException e) {
            throw new TokenException(EXPIRED_REFRESH_TOKEN);
        } catch (Exception e) {
            throw new TokenException(INVALID_REFRESH_TOKEN_VALUE);
        }
    }


    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(this.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }






}
