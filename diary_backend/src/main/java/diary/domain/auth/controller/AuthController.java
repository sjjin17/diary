package diary.domain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import diary.domain.auth.dto.Token;
import diary.domain.auth.dto.request.RefreshTokenRequestDto;
import diary.domain.auth.dto.response.TokenResponseDto;
import diary.domain.auth.service.AuthService;
import diary.global.api.BasicResponse;
import diary.global.api.CommonResponse;
import diary.global.config.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/auth")
public class AuthController {

    @Value("${jwt.expire-time.refresh-token}")
    private long EXPIRE_TIME;
    private final AuthService authService;

    private final JwtProvider jwtProvider;


    @GetMapping("/login")
    public ResponseEntity<? extends BasicResponse> login(@RequestParam String code,@RequestParam String provider, HttpServletResponse response) throws JsonProcessingException {
        Token token = authService.login(code, provider);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.refreshToken())
                .maxAge(EXPIRE_TIME)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(TokenResponseDto.builder()
                        .accessToken(token.accessToken()).build()));
    }

    @GetMapping("/reissue")
    public ResponseEntity<? extends BasicResponse> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        Token token = authService.reissue(jwtProvider.extractRefreshTokenFromCookie(request));
        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.refreshToken())
                .maxAge(jwtProvider.checkRefreshTokenExpiration(token.refreshToken()))
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(new TokenResponseDto(token.accessToken())));
    }

    @GetMapping("/test")
    public ResponseEntity<? extends BasicResponse> test() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("hello"));
    }








}
