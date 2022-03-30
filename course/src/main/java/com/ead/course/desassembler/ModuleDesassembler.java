package com.ead.course.desassembler;

import com.ead.course.input.ModuleInput;
import com.ead.course.models.ModuleModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModuleDesassembler {

    @Autowired
    private ModelMapper modelMapper;

    public ModuleModel converterModuleInputTOModule(ModuleInput moduleInput) {
        return modelMapper.map(moduleInput, ModuleModel.class);
    }



}
