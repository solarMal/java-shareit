package ru.practicum.shareit.item.itemerror.itemerrorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.itemerror.DescriptionIsRequiredException;
import ru.practicum.shareit.item.itemerror.ErrorOwnerException;
import ru.practicum.shareit.item.itemerror.ExistingItemException;
import ru.practicum.shareit.item.itemerror.ItemIsNotAvailableException;


@RestControllerAdvice
public class ItemErrorHandler {

    @ExceptionHandler(DescriptionIsRequiredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ItemErrorResponse descriptionIsRequiredException(DescriptionIsRequiredException e) {
        return new ItemErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ItemIsNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ItemErrorResponse handleItemIsNotAvailableException(ItemIsNotAvailableException e) {
        return new ItemErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ErrorOwnerException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ItemErrorResponse errorOwnerException(ErrorOwnerException e) {
        return new ItemErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ExistingItemException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ItemErrorResponse existingItemException(ExistingItemException e) {
        return new ItemErrorResponse(e.getMessage());
    }

}
