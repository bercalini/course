package com.ead.course.services.impl;

import com.ead.course.client.AuthUserClient;
import com.ead.course.dto.CourseUserDTO;
import com.ead.course.dto.UserDTO;
import com.ead.course.enums.UserStatus;
import com.ead.course.exceptions.CourseNotFoundException;
import com.ead.course.exceptions.CourseUserExist;
import com.ead.course.exceptions.UserBlokecExcpetion;
import com.ead.course.exceptions.UserNotFoundException;
import com.ead.course.input.UserInputId;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.UUID;

@Log4j2
@Service
public class CourseUserServiceImpl implements CourseUserService {
    @Autowired
    private CourseUserRepository courseUserRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AuthUserClient authUserClient;

    @Override
    @Transactional
    public CourseUserDTO save(UUID courseId, UserInputId userInputId) {
        try {
            ResponseEntity<UserDTO> userDTO = authUserClient.findByUserId(userInputId.getUserId());
            if (userDTO.getBody().getUserStatus().equals(UserStatus.BLOCKED)) {
                log.error("User com id {} is bloked", userInputId.getUserId());
                throw new UserBlokecExcpetion("User is BLOCKED");
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                log.error("User com id {} not found", userInputId.getUserId());
                throw new  UserNotFoundException("User NOT FOUND");
            }
        }
        CourseModel courseModel = courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException("Course not found"));
        if (!courseUserRepository.existsByCourseAndUserId(courseModel, userInputId.getUserId())) {
            CourseUserModel courseUserModel = CourseUserModel.builder().course(courseModel).userId(userInputId.getUserId()).build();
            CourseUserModel courseUserModelSave = courseUserRepository.save(courseUserModel);
            log.info("Save succefull");
            return converterCourseUserTOCourseUser(courseUserModelSave, courseModel, userInputId.getUserId());
        }
        log.error("Course into exist in users");
        throw new CourseUserExist("Course into exist in users");
    }

    private CourseUserDTO converterCourseUserTOCourseUser(CourseUserModel courseUserModel, CourseModel courseModel, UUID userId) {
        return CourseUserDTO.builder()
                .courseUserId(courseUserModel.getId())
                .courseId(courseModel.getCourseId())
                .userInstructor(userId)
                .description(courseModel.getDescription())
                .name(courseModel.getName())
                .build();
    }
}
