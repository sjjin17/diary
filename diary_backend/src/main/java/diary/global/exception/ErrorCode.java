package diary.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "인증 토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),

    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
