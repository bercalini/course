package com.ead.course.assembler;

import com.ead.course.dto.LessonDTO;
import com.ead.course.models.LessonModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LessonAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public LessonDTO converterLessonTOLessonDTO(LessonModel lessonModel) {
        return modelMapper.map(lessonModel,LessonDTO.class);
    }

    public List<LessonDTO> converterListLessonTOLessonDTO(List<LessonModel> lessons) {
        return lessons.stream()
                .map(l -> converterLessonTOLessonDTO(l))
                .collect(Collectors.toList());
    }

}
