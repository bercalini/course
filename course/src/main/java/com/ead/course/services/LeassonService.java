package com.ead.course.services;

import com.ead.course.dto.LessonDTO;
import com.ead.course.input.LessonInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LeassonService {
    LessonDTO save(LessonInput lessonInput);
    LessonDTO findByIdIntoModule(UUID lessonId, UUID moduleId);
    Page<LessonDTO> findAll(Pageable pageable, UUID moduleId);
    LessonDTO update(UUID lessonId, UUID moduleId, LessonInput lessonInput);
    void remove(UUID lessonId, UUID moduleId);
}
