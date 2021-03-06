package com.ead.course.dto;

import com.ead.course.models.CourseModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ModuleDTO extends RepresentationModel<ModuleDTO> {
    private UUID moduleId;
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;
    private CourseModelDTORef course;
}
