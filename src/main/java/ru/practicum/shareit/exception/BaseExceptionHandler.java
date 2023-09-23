package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class BaseExceptionHandler {

    @ExceptionHandler({AlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({ValidationException.class,
            MethodArgumentNotValidException.class,
            MissingRequestHeaderException.class,
            IncorrectStateException.class,
            IncorrectDateException.class,
            IncorrectStatusUpdateException.class,
            NotAvailableException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({OwnershipException.class})
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbidden(Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}