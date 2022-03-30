package com.ead.course.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CourseInputRef {
    @NotNull
    private UUID courseId;
}
