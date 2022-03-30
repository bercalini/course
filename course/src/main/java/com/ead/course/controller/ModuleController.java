package com.ead.course.controller;

import com.ead.course.dto.ModuleDTO;
import com.ead.course.input.ModuleInput;
import com.ead.course.services.ModuleService;
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
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleDTO save(@Valid @RequestBody ModuleInput moduleInput) {
        return moduleService.save(moduleInput);
    }

    @PutMapping("/{moduleId}")
    public ModuleDTO update(@Valid @PathVariable(name = "moduleId") UUID moduleId,
                            @RequestBody ModuleInput moduleInput) {
        return moduleService.update(moduleId, moduleInput);
    }

    @GetMapping
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
    public ModuleDTO findById(@PathVariable(name = "moduleId") UUID moduleId) {
        return moduleService.findOne(moduleId);
    }

    @DeleteMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "moduleId") UUID moduleId) {
        moduleService.delete(moduleId);
    }

    @GetMapping("/course/{courseId}")
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
    public ModuleDTO finbOneModuleIntoCourse(@PathVariable(name = "moduleId") UUID moduleId,
                                             @PathVariable(name = "courseId") UUID courseId) {
        return moduleService.findByIdIntoCourseId(moduleId, courseId);
    }

}
