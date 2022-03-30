package com.ead.course.services;

import com.ead.course.dto.ModuleDTO;
import com.ead.course.input.ModuleInput;
import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ModuleService {
    void delete(UUID moduleId);
    ModuleDTO save(ModuleInput moduleInput);
    ModuleDTO update(UUID moduleId, ModuleInput moduleInput);
    Page<ModuleDTO> getAll(Pageable pageable);
    ModuleDTO findOne(UUID moduleId);
    List<ModuleDTO> findModulesByCourseId(UUID courseId);
    ModuleDTO findByIdIntoCourseId(UUID moduleId, UUID courseId);
}
