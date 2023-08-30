package ru.practicum.shareit.item.itemerror.itemerrorhandler;

public class ItemErrorResponse {
    private final String error;

    public ItemErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
