package com.ead.course.exceptions;

public class LessonNotFoundExcpetion extends RuntimeException {

    public LessonNotFoundExcpetion(String mensagem) {
        super(mensagem);
    }
}
