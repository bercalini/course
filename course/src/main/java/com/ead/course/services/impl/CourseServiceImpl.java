package com.ead.course.services.impl;

import com.ead.course.assembler.CourseAssembler;
import com.ead.course.desassembler.CourseDesassembler;
import com.ead.course.dto.CourseDTO;
import com.ead.course.exceptions.CourseNotFoundException;
import com.ead.course.input.CourseInput;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseDesassembler courseDesassembler;

    @Autowired
    private CourseAssembler courseAssembler;


    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> modules = moduleRepository.findAllModuleIntoCourse(courseModel.getCourseId());
        if(!modules.isEmpty()) {
            modules.stream().forEach(m -> {
                List<LessonModel> lessons = lessonRepository.findAllLessonsIntoModule(m.getModuleId());
                if(!lessons.isEmpty()) {
                    lessonRepository.deleteAll(lessons);
                }
            });
            moduleRepository.deleteAll(modules);
        }
        courseRepository.delete(courseModel);
    }

    @Override
    public CourseDTO save(CourseInput courseInput) {
        CourseModel courseModel = courseDesassembler.converterCourseInputTOCourse(courseInput);
        CourseModel courseModelSave = courseRepository.save(courseModel);
        return courseAssembler.converterCourseModelTOCourseDTO(courseModelSave);
    }

    @Override
    public Page<CourseDTO> findAll(Specification<CourseModel> courseSpec, Pageable pageable) {
        Page<CourseModel> pagesCourseModels = courseRepository.findAll(courseSpec, pageable);
        List<CourseDTO> courseDTOSList = courseAssembler.converterListCourseTOCourseDTO(pagesCourseModels.toList());
        return new PageImpl<>(courseDTOSList, pageable, pagesCourseModels.getTotalElements());
    }


    @Override
    public CourseDTO findById(UUID courseId) {
       return courseAssembler.converterCourseModelTOCourseDTO(courseRepository.findById(courseId)
               .orElseThrow(() -> new CourseNotFoundException("Course NOT Found")));
    }

    @Override
    public CourseDTO update(UUID courseId, CourseInput courseInput) {
        Optional<CourseModel> courseModel = courseRepository.findById(courseId);
        BeanUtils.copyProperties(courseInput, courseModel.get(), "courseId", "creationDate");
        return courseAssembler.converterCourseModelTOCourseDTO(courseRepository.save(courseModel.get()));
    }


}
