package com.matjar.user.controller;


import com.matjar.user.model.Configuration;
import com.matjar.user.service.ConfigurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/configuration")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping("")
    public ResponseEntity<?> getConfigurations() {
        List<Configuration> configurations = configurationService.loadConfigurations();
        return ResponseEntity.ok(configurations);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshConfiguration() {
        configurationService.refreshCache();
        return ResponseEntity.ok("");
    }

}
