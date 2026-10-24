package ru.Novoselov_Leonid.LocalChat.exception;

public class ApiErrorResponse {
    private String message;

    private ApiErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ApiErrorResponse create(Throwable e) {
        return new ApiErrorResponse(e.getMessage());
    }

    public static ApiErrorResponse create(String message) {
        return new ApiErrorResponse(message);
    }
}
