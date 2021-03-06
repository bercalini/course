package com.ead.course.services;

import com.ead.course.dto.CourseDTO;
import com.ead.course.input.CourseInput;
import com.ead.course.models.CourseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface CourseService {
    void delete(CourseModel courseModel);
    CourseDTO save(CourseInput courseInput);
    Page<CourseDTO> findAll(Specification<CourseModel> courseSpec, Pageable pageable);
    CourseDTO findById(UUID courseId);
    CourseDTO update(UUID courseId, CourseInput courseInput);
}
