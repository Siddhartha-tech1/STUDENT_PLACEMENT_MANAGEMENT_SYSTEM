package com.placement.spms.controller;

import com.placement.spms.dto.OptionsDtos.AppOptions;
import com.placement.spms.service.OptionsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/options")
public class OptionsController {
    private final OptionsService optionsService;

    public OptionsController(OptionsService optionsService) {
        this.optionsService = optionsService;
    }

    @GetMapping
    public AppOptions options() {
        return optionsService.options();
    }
}
