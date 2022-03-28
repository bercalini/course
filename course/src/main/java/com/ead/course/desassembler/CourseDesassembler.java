package com.ead.course.desassembler;

import com.ead.course.input.CourseInput;
import com.ead.course.models.CourseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseDesassembler {

    @Autowired
    private ModelMapper modelMapper;

    public CourseModel converterCourseInputTOCourse(CourseInput courseInput) {
        return modelMapper.map(courseInput, CourseModel.class);
    }
}
