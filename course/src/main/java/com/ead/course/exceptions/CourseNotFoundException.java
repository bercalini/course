package com.ead.course.exceptions;

public class CourseNotFoundException extends RuntimeException{

    public CourseNotFoundException(String mensagem) {
        super(mensagem);
    }
}
