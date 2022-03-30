package com.ead.course.dto;

import com.ead.course.models.CourseModel;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ModuleDTO extends RepresentationModel<ModuleDTO> {
    private UUID moduleId;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private CourseModelDTORef course;
}
