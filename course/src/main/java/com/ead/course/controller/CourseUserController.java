package com.ead.course.controller;

import com.ead.course.client.AuthUserClient;
import com.ead.course.dto.CourseUserDTO;
import com.ead.course.dto.UserDTO;
import com.ead.course.input.UserInputId;
import com.ead.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    private AuthUserClient courseClient;

    @Autowired
    private CourseUserService courseUserService;


    @GetMapping("/courses/{courseId}/users")
    public Page<UserDTO> findAllUsersCourseId(@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                @PathVariable(value = "courseId")UUID courseId) {
        return courseClient.findCourseById(courseId, pageable);
    }

    @PostMapping("/courses/{courseId}/users/save")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseUserDTO saveCourseIntoUserId(@PathVariable(value = "courseId")UUID courseId,
                                              @RequestBody @Valid UserInputId userInputId) {
        return courseUserService.save(courseId, userInputId);
    }


}
