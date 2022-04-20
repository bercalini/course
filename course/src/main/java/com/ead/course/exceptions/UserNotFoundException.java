package com.ead.course.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String mensagem) {
        super(mensagem);
    }
}
