package com.ead.course.controller;

import com.ead.course.dto.CourseDTO;
import com.ead.course.input.CourseInput;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "API para cadastrar cursos", response = CourseDTO.class,
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "courso cadastrado com sucesso", response = CourseDTO.class),
        @ApiResponse(code = 401, message = "Erro com a autorização com a API"),
        @ApiResponse(code = 403, message = "Erro com a permissão com a API")})
    public CourseDTO save(@RequestBody @Valid CourseInput courseInput) {
        return courseService.save(courseInput);
    }

    @GetMapping
    @ApiOperation(value = "API para listar os cursos", response = CourseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Busca de Cursos", response = CourseDTO.class),
            @ApiResponse(code = 401, message = "Erro com a autorização com a API"),
            @ApiResponse(code = 403, message = "Erro com a permissão com a API"),
    })
    public Page<CourseDTO> findAll(SpecificationTemplate.CourseSpec courseSpec,
            @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) UUID userId) {
        Page<CourseDTO> pagesCourseDTO = null;
        if(userId != null) {
            pagesCourseDTO = courseService.findAll(SpecificationTemplate.courseModelUserId(userId).and(courseSpec), pageable);
        } else {
            pagesCourseDTO = courseService.findAll(courseSpec, pageable);
        }
        if(!pagesCourseDTO.isEmpty()) {
            pagesCourseDTO.toList().stream().forEach(course -> {
                course.add(linkTo(methodOn(CourseController.class).findById(course.getCourseId())).withSelfRel());
            });
        }
        return pagesCourseDTO;
    }

    @GetMapping("/{courseId}")
    @ApiOperation(value = "API para buscar por id o curso", response = CourseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Busca de Curso", response = CourseDTO.class),
            @ApiResponse(code = 401, message = "Erro com a autorização com a API"),
            @ApiResponse(code = 403, message = "Erro com a permissão com a API"),
            @ApiResponse(code = 404, message = "Curso não encontrado")
    })
    public CourseDTO findById(@PathVariable(value = "courseId")UUID courseId) {
        return courseService.findById(courseId);
    }

    @PutMapping("/{courseId}")
    @ApiOperation(value = "API para editar o curso", response = CourseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Curso editado com sucesso", response = CourseDTO.class),
            @ApiResponse(code = 401, message = "Erro com a autorização com a API"),
            @ApiResponse(code = 403, message = "Erro com a permissão com a API"),
            @ApiResponse(code = 404, message = "Curso nao encontrado")
    })
    public CourseDTO update(@PathVariable(value = "courseId") UUID courseId,
                                @RequestBody CourseInput courseInput) {
        return courseService.update(courseId, courseInput);
    }

    @DeleteMapping("{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "API para remover o curso")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Curso removido com sucesso"),
            @ApiResponse(code = 401, message = "Erro com a autorização com a API"),
            @ApiResponse(code = 403, message = "Erro com a permissão com a API"),
            @ApiResponse(code = 404, message = "Curso nao encontrado")
    })
    public void remove(@PathVariable(value = "courseId") UUID courseId) {
        CourseModel courseModel = new CourseModel();
        CourseDTO courseDTO = courseService.findById(courseId);
        BeanUtils.copyProperties(courseDTO, courseModel);
        courseService.delete(courseModel);
    }
}
