package todaktodak.global.api;

import lombok.Getter;

@Getter
public class CommonResponse<T> extends BasicResponse {
    private boolean success;
    private T data;

    public CommonResponse(T data) {
        this.success = true;
        this.data = data;
    }

    public CommonResponse<T> ok(T data) {
        return new CommonResponse<>(data);
    }
}
