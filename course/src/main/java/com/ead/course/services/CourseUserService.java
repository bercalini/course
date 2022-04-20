package com.ead.course.services;

import com.ead.course.dto.CourseUserDTO;
import com.ead.course.input.UserInputId;

import java.util.UUID;

public interface CourseUserService {
    CourseUserDTO save(UUID courseId, UserInputId userInputId);
}
