package todaktodak.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 Access Token 입니다. "),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 Refresh Token 입니다."),
    INVALID_ACCESS_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "유효하지 않은 Access Token 입니다."),
    INVALID_REFRESH_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token 입니다."),

    UNAUTHORIZED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    UNAUTHORIZED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 필요합니다."),
    NO_ACCESS(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    USER_NOT_EXISTS(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),
    NOT_FOUND_IMAGE_PATH(HttpStatus.NOT_FOUND, "이미지 경로를 찾을 수 없습니다."),
    NOT_FOUND_DIARY(HttpStatus.NOT_FOUND, "다이어리를 찾을 수 없습니다."),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");



    private final HttpStatus httpStatus;
    private final String message;
}
