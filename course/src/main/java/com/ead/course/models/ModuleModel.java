package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_MODULES")
public class ModuleModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID moduleId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 150)
    private String description;

    @Column(nullable = false)
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CourseModel course;

    @OneToMany(mappedBy = "module")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<LessonModel> lessons = new HashSet<>();

}
