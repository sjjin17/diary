package diary.global.api;

public class ErrorResponse extends BasicResponse{
    private boolean success;
    private String message;

    public ErrorResponse(String message) {
        success = false;
        this.message = message;
    }

}
