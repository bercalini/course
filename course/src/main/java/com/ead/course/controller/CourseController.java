package com.ead.course.controller;

import com.ead.course.dto.CourseDTO;
import com.ead.course.input.CourseInput;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO save(@RequestBody @Valid CourseInput courseInput) {
        return courseService.save(courseInput);
    }

    @GetMapping
    public Page<CourseDTO> findAll(@PageableDefault(page = 0, size = 10, sort = "creationDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CourseDTO> pagesCourseDTO = courseService.findAll(pageable);
        if(!pagesCourseDTO.isEmpty()) {
            pagesCourseDTO.toList().stream().forEach(course -> {
                course.add(linkTo(methodOn(CourseController.class).findById(course.getCourseId())).withSelfRel());
            });
        }
        return pagesCourseDTO;
    }

    @GetMapping("/{courseId}")
    public CourseDTO findById(@PathVariable(value = "courseId")UUID courseId) {
        return courseService.findById(courseId);
    }

    @PutMapping("/{courseId}")
    public CourseDTO update(@PathVariable(value = "courseId") UUID courseId,
                                @RequestBody CourseInput courseInput) {
        return courseService.update(courseId, courseInput);
    }

    @DeleteMapping("{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable(value = "courseId") UUID courseId) {
        CourseModel courseModel = new CourseModel();
        CourseDTO courseDTO = courseService.findById(courseId);
        BeanUtils.copyProperties(courseDTO, courseModel);
        courseService.delete(courseModel);
    }


}
