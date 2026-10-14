package com.placement.spms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApiStatusController {

    @GetMapping("/")
    public Map<String, String> status() {
        return Map.of(
                "status", "ok",
                "service", "Student Placement Management System API",
                "options", "/api/options"
        );
    }
}
