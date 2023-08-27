package ru.practicum.shareit.user.usererror.usererrorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.user.usererror.InvalidEmailException;
import ru.practicum.shareit.user.usererror.UserAlreadyExistException;
import ru.practicum.shareit.user.usererror.UserNotFoundException;

@RestControllerAdvice
public class UserErrorHandler {

    @ExceptionHandler(InvalidEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public UserErrorResponse handleInvalidEmailException(InvalidEmailException e) {
        return new UserErrorResponse(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return new UserErrorResponse(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public UserErrorResponse handleUserAlreadyExistException(UserAlreadyExistException e) {
        return new UserErrorResponse(e.getMessage());
    }


}
