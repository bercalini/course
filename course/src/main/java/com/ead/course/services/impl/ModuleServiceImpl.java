package com.ead.course.services.impl;

import com.ead.course.assembler.ModuleAssembler;
import com.ead.course.desassembler.ModuleDesassembler;
import com.ead.course.dto.ModuleDTO;
import com.ead.course.exceptions.CourseNotFoundException;
import com.ead.course.exceptions.ModuleNotFoundExcpetion;
import com.ead.course.input.ModuleInput;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleAssembler moduleAssembler;

    @Autowired
    private ModuleDesassembler moduleDesassembler;

    @Transactional
    @Override
    public void delete(UUID moduleId) {
        ModuleModel moduleModel = findById(moduleId);
        List<LessonModel> lessons = lessonRepository.findAllLessonsIntoModule(moduleId);
        if(!lessons.isEmpty()) {
            lessonRepository.deleteAll(lessons);
        }
        moduleRepository.delete(moduleModel);
    }

    @Override
    public ModuleDTO save(ModuleInput moduleInput) {
        CourseModel courseModel = courseFindById(moduleInput.getCourse().getCourseId());
        ModuleModel moduleModel = moduleDesassembler.converterModuleInputTOModule(moduleInput);
        moduleModel.setCourse(courseModel);
        return moduleAssembler.converterModuleTOModuleDTO(moduleRepository.save(moduleModel));
    }

    @Override
    public ModuleDTO update(UUID moduleId, ModuleInput moduleInput) {
        ModuleModel moduleModel = findById(moduleId);
        CourseModel courseModel = courseFindById(moduleInput.getCourse().getCourseId());
        moduleModel.setCourse(courseModel);
        BeanUtils.copyProperties(moduleInput, moduleModel, "moduleId", "creationDate");
        return moduleAssembler.converterModuleTOModuleDTO(moduleRepository.save(moduleModel));
    }

    @Override
    public Page<ModuleDTO> getAll(Pageable pageable) {
        Page<ModuleModel> modulePages = moduleRepository.findAll(pageable);
        List<ModuleDTO> listModulesDTO = moduleAssembler.converterListModuleTOModuleDTO(modulePages.toList());
        return new PageImpl<>(listModulesDTO, pageable, modulePages.getTotalElements());
    }

    @Override
    public ModuleDTO findOne(UUID moduleId) {
        return moduleAssembler.converterModuleTOModuleDTO(findById(moduleId));
    }

    @Override
    public List<ModuleDTO> findModulesByCourseId(UUID courseId) {
        List<ModuleModel> modulesModels = moduleRepository.findAllModuleIntoCourse(courseId);
        return moduleAssembler.converterListModuleTOModuleDTO(modulesModels);
    }

    @Override
    public ModuleDTO findByIdIntoCourseId(UUID moduleId, UUID courseId) {
        Optional<ModuleModel> moduleModel = moduleRepository.findByIdIntoCourseId(moduleId, courseId);
        return moduleAssembler.converterModuleTOModuleDTO(moduleModel.get());
    }

    private CourseModel courseFindById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course Not Found"));
    }

    private ModuleModel findById(UUID moduleId) {
        return moduleRepository.findById(moduleId).orElseThrow(()
                -> new ModuleNotFoundExcpetion("MODULE NOT FOUND"));
    }

}
