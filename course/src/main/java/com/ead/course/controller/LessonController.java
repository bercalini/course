package com.ead.course.controller;

import com.ead.course.dto.LessonDTO;
import com.ead.course.input.LessonInput;
import com.ead.course.services.LeassonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/lessons")
@Api(tags = "/lessons")
public class LessonController {

    @Autowired
    private LeassonService leassonService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Api para salvar a lição", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,
        response = LessonDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Lição criada com sucesso", response = LessonDTO.class),
            @ApiResponse(code = 401, message = "erro com a permissão com a API"),
            @ApiResponse(code = 403, message = "erro com a auorização com a API"),
            @ApiResponse(code = 404, message = "Id do module não encontrado")
    })
    public LessonDTO save(@Valid @RequestBody LessonInput lessonInput) {
        LessonDTO lessonDTO = leassonService.save(lessonInput);
        lessonDTO.add(linkTo(methodOn(LessonController.class).findByIdIntoModule(lessonDTO.getLessonId(), lessonDTO.getModule().getModuleId())).withSelfRel());
        return lessonDTO;
    }

    @PutMapping("/{lessonId}/module/{moduleId}")
    @ApiOperation(value = "Api para atualizar lesson por module", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, response = LessonDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Atualizado com sucesso", response = LessonDTO.class),
            @ApiResponse(code = 401, message = "erro com a permissão com a API"),
            @ApiResponse(code = 403, message = "erro com a auorização com a API"),
            @ApiResponse(code = 404, message = "Id do module não encontrado ou da lição")
    })
    public LessonDTO update(@Valid @PathVariable(value = "lessonId")UUID lessonId,
                            @PathVariable(value = "moduleId")UUID moduleId, @RequestBody LessonInput lessonInput) {
        LessonDTO lessonDTO = leassonService.update(lessonId, moduleId, lessonInput);
        return lessonDTO.add(linkTo(methodOn(LessonController.class).findByIdIntoModule(lessonDTO.getLessonId(), lessonDTO.getModule().getModuleId())).withSelfRel());
    }

    @GetMapping("/module/{moduleId}")
    @ApiOperation(value = "Api para buscar lesson por module", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, response = LessonDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Busca com sucesso", response = LessonDTO.class),
            @ApiResponse(code = 401, message = "erro com a permissão com a API"),
            @ApiResponse(code = 403, message = "erro com a auorização com a API"),
            @ApiResponse(code = 404, message = "Id do module não encontrado ou da lição")
    })
    public Page<LessonDTO> findAll(Pageable pageable, @PathVariable(value = "moduleId")UUID moduleId) {
        Page<LessonDTO> pagesLessons = leassonService.findAll(pageable, moduleId);
        if(!pagesLessons.isEmpty()) {
            pagesLessons.stream().forEach(p -> {
                p.add(linkTo(methodOn(LessonController.class).findByIdIntoModule(p.getLessonId(), p.getModule().getModuleId())).withSelfRel());
            });
        }
        return pagesLessons;
    }

    @GetMapping("/{lessonId}/module/{moduleId}")
    @ApiOperation(value = "Api para buscar lesson por module", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, response = LessonDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Busca com sucesso", response = LessonDTO.class),
            @ApiResponse(code = 401, message = "erro com a permissão com a API"),
            @ApiResponse(code = 403, message = "erro com a auorização com a API"),
            @ApiResponse(code = 404, message = "Id do module não encontrado ou da lição")
    })
    public LessonDTO findByIdIntoModule(@PathVariable(value = "lessonId")UUID lessonId,
                                        @PathVariable(value = "moduleId")UUID moduleId) {
        LessonDTO lessonDTO = leassonService.findByIdIntoModule(lessonId, moduleId);
        return lessonDTO.add(linkTo(methodOn(LessonController.class).findByIdIntoModule(lessonDTO.getLessonId(), lessonDTO.getModule().getModuleId())).withSelfRel());
    }

    @DeleteMapping("/{lessonId}/module/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Api para remover lesson por module")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Removido com sucesso"),
            @ApiResponse(code = 401, message = "erro com a permissão com a API"),
            @ApiResponse(code = 403, message = "erro com a auorização com a API"),
            @ApiResponse(code = 404, message = "Id do module não encontrado ou da lição")
    })
    public void remove(@PathVariable(value = "lessonId")UUID lessonId, @PathVariable(value = "moduleId")UUID moduleId) {
        leassonService.remove(lessonId, moduleId);
    }

}
