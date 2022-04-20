package com.ead.course.exceptions;

public class CourseUserExist extends RuntimeException{

    public CourseUserExist(String mensgem) {
        super(mensgem);
    }
}
