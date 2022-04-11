package com.ead.course.controller;

import com.ead.course.client.CourseClient;
import com.ead.course.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    private CourseClient courseClient;

    //ESSE AUI É NOVOO!
    @GetMapping("/courses/{courseId}/users")
    public Page<UserDTO> findAllUsersCourseId(@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                @PathVariable(value = "courseId")UUID courseId) {
        System.out.println("PASSOU");
        return courseClient.findCourseById(courseId, pageable);
    }

}
