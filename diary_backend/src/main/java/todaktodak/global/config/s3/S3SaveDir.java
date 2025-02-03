package todaktodak.global.config.s3;

import lombok.AllArgsConstructor;
import todaktodak.global.exception.CustomException;
import todaktodak.global.exception.ErrorCode;

@AllArgsConstructor
public enum S3SaveDir {
    USER("/user/profile"),
    DIARY("/diary"),
    POST("/post");

    public final String path;

    public static S3SaveDir of(String stringParam) {
        return switch (stringParam.toLowerCase()) {
            case "user" -> USER;
            case "diary" -> DIARY;
            case "post" -> POST;

            default -> throw new CustomException(ErrorCode.NOT_FOUND_IMAGE_PATH);

        };
    }
}
