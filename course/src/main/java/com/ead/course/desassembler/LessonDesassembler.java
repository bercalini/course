package com.ead.course.desassembler;

import com.ead.course.input.LessonInput;
import com.ead.course.models.LessonModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonDesassembler {

    @Autowired
    private ModelMapper modelMapper;

    public LessonModel converterLessonInputTOLesson(LessonInput lessonInput) {
        return modelMapper.map(lessonInput, LessonModel.class);
    }


}
