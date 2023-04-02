package br.inatel.thisismeapi.controllers;

import br.inatel.thisismeapi.exceptions.*;
import br.inatel.thisismeapi.exceptions.mongo.UniqueViolationConstraintException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.DateTimeException;
import java.time.Instant;

@ControllerAdvice
public class GlobalControllerExceptions {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptions.class);

    @ExceptionHandler({
            ConstraintViolationException.class,
            OnCreateDataException.class,
            QuestValidationsException.class,
            IllegalArgumentException.class,
            DateTimeException.class,
            AlreadyDoneException.class,
            OnCheckSubQuestException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardError> badRequestHandler(Exception e, HttpServletRequest request) {

        LOGGER.info("m=badRequestHandler, statusCode={}, msg={}", HttpStatus.BAD_REQUEST.value(), e.getMessage());
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now().toString());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({TokenExpiredException.class, TokenInvalidException.class, UnregisteredUserException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<StandardError> unauthorizedHandler(Exception e, HttpServletRequest request) {

        LOGGER.error("m=unauthorizedHandler, statusCode={}, msg={}", HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now().toString());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler({UniqueViolationConstraintException.class, SkillAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<StandardError> conflictHandler(Exception e, HttpServletRequest request) {
        LOGGER.info("m=conflictHandler, statusCode={}, msg={}", HttpStatus.CONFLICT.value(), e.getMessage());
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now().toString());
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setError(HttpStatus.CONFLICT.getReasonPhrase());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler({ValidationsParametersException.class, SendEmailException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<StandardError> internalServerErrorHandler(Exception e, HttpServletRequest request) {
        LOGGER.info("m=internalServerErrorHandler, statusCode={}, msg={}", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now().toString());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardError> notFoundHandler(Exception e, HttpServletRequest request) {
        LOGGER.info("m=notFoundHandler, statusCode={}, msg={}", HttpStatus.NOT_FOUND.value(), e.getMessage());
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now().toString());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
