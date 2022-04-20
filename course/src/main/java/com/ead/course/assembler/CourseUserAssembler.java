package com.ead.course.assembler;

import com.ead.course.dto.CourseUserDTO;
import com.ead.course.models.CourseUserModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseUserAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public CourseUserDTO converterCourseUserTOCourseUserDTO(CourseUserModel courseUserModel) {
        return modelMapper.map(courseUserModel, CourseUserDTO.class);
    }
}
