package com.ead.course.services.impl;

import com.ead.course.assembler.LessonAssembler;
import com.ead.course.desassembler.LessonDesassembler;
import com.ead.course.dto.LessonDTO;
import com.ead.course.exceptions.LessonNotFoundExcpetion;
import com.ead.course.exceptions.ModuleNotFoundExcpetion;
import com.ead.course.input.LessonInput;
import com.ead.course.models.LessonModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.LeassonService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LeassonServiceImpl implements LeassonService {

    @Autowired
    private LessonAssembler lessonAssembler;

    @Autowired
    private LessonDesassembler lessonDesassembler;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Override
    public LessonDTO save(LessonInput lessonInput) {
        moduleRepository.findById(lessonInput.getModule().getModuleId()).
                orElseThrow(() -> new ModuleNotFoundExcpetion("Module NOT FOUND"));

        LessonModel lessonModel = lessonDesassembler.converterLessonInputTOLesson(lessonInput);
        return lessonAssembler.converterLessonTOLessonDTO(lessonRepository.save(lessonModel));
    }

    @Override
    public LessonDTO findByIdIntoModule(UUID lessonId, UUID moduleId) {
        return lessonAssembler.converterLessonTOLessonDTO(lessonRepository.findByLessonIntoModuleId(lessonId, moduleId)
                .orElseThrow(() -> new LessonNotFoundExcpetion("Lesson NOT FOUND OR MODULE")));
    }

    @Override
    public Page<LessonDTO> findAll(Specification<LessonModel> lessonSpec, Pageable pageable) {
        Page<LessonModel> pagesLessons = lessonRepository.findAll(lessonSpec, pageable);
        List<LessonDTO> lessonsDTO = lessonAssembler.converterListLessonTOLessonDTO(pagesLessons.toList());
        return new PageImpl<>(lessonsDTO, pageable, pagesLessons.getTotalElements());
    }

    @Override
    public LessonDTO update(UUID lessonId, UUID moduleId, LessonInput lessonInput) {
        LessonModel lessonModel = lessonRepository.findByLessonIntoModuleId(lessonId, moduleId)
                .orElseThrow(() -> new LessonNotFoundExcpetion("Lesson NOT FOUND"));

        BeanUtils.copyProperties(lessonInput, lessonModel, "lessonId", "creationDate");
        return lessonAssembler.converterLessonTOLessonDTO(lessonRepository.save(lessonModel));
    }

    @Override
    public void remove(UUID lessonId, UUID moduleId) {
        LessonModel lessonModel = lessonRepository.findByLessonIntoModuleId(lessonId, moduleId)
                .orElseThrow(() -> new LessonNotFoundExcpetion("Lesson NOT FOUND"));
        lessonRepository.delete(lessonModel);
    }
}
