package com.ead.course.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CourseUserDTO {
    private UUID courseUserId;
    private UUID courseId;
    private String name;
    private String description;
    private UUID userInstructor;
}
