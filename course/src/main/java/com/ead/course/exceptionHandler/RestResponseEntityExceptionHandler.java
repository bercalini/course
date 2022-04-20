package com.ead.course.exceptionHandler;

import com.ead.course.exceptionHandler.problema.Problema;
import com.ead.course.exceptions.CourseNotFoundException;
import com.ead.course.exceptions.CourseUserExist;
import com.ead.course.exceptions.UserBlokecExcpetion;
import com.ead.course.exceptions.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> UserNotFoundHandle(Exception ex, WebRequest request) {
        Problema problema = Problema.builder().mensagem(ex.getMessage()).localDateTime(LocalDateTime.now()).status(HttpStatus.NOT_FOUND.value()).build();
        return handleExceptionInternal(ex, problema, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UserBlokecExcpetion.class)
    protected ResponseEntity<Object> UserBlockedHandle(Exception ex, WebRequest request) {
        Problema problema = Problema.builder().mensagem(ex.getMessage()).localDateTime(LocalDateTime.now()).status(HttpStatus.CONFLICT.value()).build();
        return handleExceptionInternal(ex, problema, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(CourseUserExist.class)
    protected ResponseEntity<Object> CourseUserExistHandle(Exception ex, WebRequest request) {
        Problema problema = Problema.builder().mensagem(ex.getMessage()).localDateTime(LocalDateTime.now()).status(HttpStatus.CONFLICT.value()).build();
        return handleExceptionInternal(ex, problema, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    protected ResponseEntity<Object> CourseNotFoundExceptionHandle(Exception ex, WebRequest request) {
        Problema problema = Problema.builder().mensagem(ex.getMessage()).localDateTime(LocalDateTime.now()).status(HttpStatus.NOT_FOUND.value()).build();
        return handleExceptionInternal(ex, problema, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
