package ru.practicum.shareit.user.usererror.usererrorhandler;

public class UserErrorResponse {
    private final String error;

    public UserErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
