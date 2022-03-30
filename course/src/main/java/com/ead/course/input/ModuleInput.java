package com.ead.course.input;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ModuleInput {

    @NotBlank
    @Size(min = 5, max = 250)
    private String title;
    @NotBlank
    @Size(min = 5, max = 250)
    private String description;

    private CourseInputRef course;
}
