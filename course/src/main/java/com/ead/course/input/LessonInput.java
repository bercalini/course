package com.ead.course.input;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LessonInput {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String videoUrl;

    private ModuleInputRef module;

}
