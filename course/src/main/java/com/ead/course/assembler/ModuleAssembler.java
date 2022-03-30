package com.ead.course.assembler;

import com.ead.course.dto.ModuleDTO;
import com.ead.course.models.ModuleModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModuleAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public ModuleDTO converterModuleTOModuleDTO(ModuleModel moduleModel) {
        return modelMapper.map(moduleModel,ModuleDTO.class);
    }

    public List<ModuleDTO> converterListModuleTOModuleDTO(List<ModuleModel> modulos) {
        return modulos.stream()
                .map(m -> converterModuleTOModuleDTO(m))
                .collect(Collectors.toList());
    }

}
