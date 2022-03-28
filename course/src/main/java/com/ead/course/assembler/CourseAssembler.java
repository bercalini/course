package com.ead.course.assembler;

import com.ead.course.dto.CourseDTO;
import com.ead.course.models.CourseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CourseAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public CourseDTO converterCourseModelTOCourseDTO(CourseModel courseModel) {
        return modelMapper.map(courseModel, CourseDTO.class);
    }

    public List<CourseDTO> converterListCourseTOCourseDTO(List<CourseModel> courses) {
        return courses.stream()
                .map(c -> converterCourseModelTOCourseDTO(c))
                .collect(Collectors.toList());
    }

}
