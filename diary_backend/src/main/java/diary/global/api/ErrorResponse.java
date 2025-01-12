package diary.global.api;

import diary.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class ErrorResponse extends BasicResponse{
    private boolean success;
    private int status;
    private String message;

    public ErrorResponse(HttpStatus status, String message) {
        success = false;
        this.status = status.value();
        this.message = message;
    }

    public static ErrorResponse from(ErrorCode code) {
        return new ErrorResponse(code.getHttpStatus(), code.getMessage());
    }

}
