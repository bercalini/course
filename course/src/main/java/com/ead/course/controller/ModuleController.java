package com.ead.course.controller;

import com.ead.course.dto.ModuleDTO;
import com.ead.course.input.ModuleInput;
import com.ead.course.services.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/modules")
@Api(tags = "/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "API para salvar o module", response = ModuleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Modulo criado com sucesso", response = ModuleDTO.class),
            @ApiResponse(code = 401, message = "Erro com a autorização com a API"),
            @ApiResponse(code = 403, message = "Erro com a permissão com a API"),
    })
    public ModuleDTO save(@Valid @RequestBody ModuleInput moduleInput) {
        ModuleDTO moduleDTOSave = moduleService.save(moduleInput);
        moduleDTOSave.add(linkTo(methodOn(ModuleController.class).findById(moduleDTOSave.getModuleId())).withSelfRel());
        return moduleDTOSave;
    }

    @PutMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "API para atualizar o Modulo", response = ModuleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Modulo editado com sucesso", response = ModuleDTO.class),
            @ApiResponse(code = 401, message = "Erro com a autorização com a API"),
            @ApiResponse(code = 403, message = "Erro com a permissão com a API"),
            @ApiResponse(code = 404, message = "Modulo não encontrado"),
    })
    public ModuleDTO update(@Valid @PathVariable(name = "moduleId") UUID moduleId,
                            @RequestBody ModuleInput moduleInput) {
        ModuleDTO moduleDTO = moduleService.update(moduleId, moduleInput);
        moduleDTO.add(linkTo(methodOn(ModuleController.class).findById(moduleDTO.getModuleId())).withSelfRel());
        return moduleDTO;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "API para buscar os modulos", response = ModuleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Busca com sucesso", response = ModuleDTO.class),
            @ApiResponse(code = 401, message = "Erro com a autorização com a API"),
            @ApiResponse(code = 403, message = "Erro com a permissão com a API"),
    })
    public Page<ModuleDTO> getAllModules(@PageableDefault(page = 0, size = 10, sort = "creationDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ModuleDTO> modules = moduleService.getAll(pageable);
        if(!modules.isEmpty()) {
            modules.stream().forEach(m -> {
                m.add(linkTo(methodOn(ModuleController.class).findById(m.getModuleId())).withSelfRel());
            });
        }
        return modules;
    }

    @GetMapping("/{moduleId}")
    @ApiOperation(value = "API para buscar o modulo por id", response = ModuleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Busca com sucesso", response = ModuleDTO.class),
            @ApiResponse(code = 401, message = "Erro com a autorização com a API"),
            @ApiResponse(code = 403, message = "Erro com a permissão com a API"),
            @ApiResponse(code = 404, message = "Modulo não encontrado"),
    })
    public ModuleDTO findById(@PathVariable(name = "moduleId") UUID moduleId) {
        ModuleDTO moduleDTO = moduleService.findOne(moduleId);
        moduleDTO.add(linkTo(methodOn(ModuleController.class).findById(moduleDTO.getModuleId())).withSelfRel());
        return  moduleDTO;
    }

    @DeleteMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "API para remover o Modulo")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Modulo removido com sucesso"),
            @ApiResponse(code = 401, message = "Erro com a autorização com a API"),
            @ApiResponse(code = 403, message = "Erro com a permissão com a API"),
            @ApiResponse(code = 404, message = "Modulo não encontrado"),
    })
    public void delete(@PathVariable(name = "moduleId") UUID moduleId) {
        moduleService.delete(moduleId);
    }

    @GetMapping("/course/{courseId}")
    @ApiOperation(value = "API para listar os modulos por id do curso", response = ModuleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Busca com sucesso", response = ModuleDTO.class),
            @ApiResponse(code = 401, message = "Erro com a autorização com a API"),
            @ApiResponse(code = 403, message = "Erro com a permissão com a API"),
            @ApiResponse(code = 404, message = "Modulo não encontrado ou curso nao encontrado"),
    })
    public List<ModuleDTO> findAllModulesIntoCourse(@PathVariable(name = "courseId") UUID courseId) {
        List<ModuleDTO> modules = moduleService.findModulesByCourseId(courseId);
        if(!modules.isEmpty()) {
            modules.stream().forEach(m -> {
                m.add(linkTo(methodOn(ModuleController.class).finbOneModuleIntoCourse(m.getModuleId(), m.getCourse().getCourseId())).withSelfRel());
            });
        }
        return modules;
    }

    @GetMapping("module/{moduleId}/course/{courseId}")
    @ApiOperation(value = "API para buscar o modulo por id do curso", response = ModuleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "modulo retornado com sucesso", response = ModuleDTO.class),
            @ApiResponse(code = 401, message = "Erro com a autorização com a API"),
            @ApiResponse(code = 403, message = "Erro com a permissão com a API"),
            @ApiResponse(code = 404, message = "Modulo não encontrado ou curso nao encontrado"),
    })
    public ModuleDTO finbOneModuleIntoCourse(@PathVariable(name = "moduleId") UUID moduleId,
                                             @PathVariable(name = "courseId") UUID courseId) {
        ModuleDTO moduleDTO = moduleService.findByIdIntoCourseId(moduleId, courseId);
        moduleDTO.add(linkTo(methodOn(ModuleController.class).finbOneModuleIntoCourse(moduleDTO.getModuleId(), moduleDTO.getCourse().getCourseId())).withSelfRel());
        return moduleDTO;
    }

}
