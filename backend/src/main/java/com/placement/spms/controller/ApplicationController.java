package com.placement.spms.controller;

import com.placement.spms.dto.ApplicationDtos.*;
import com.placement.spms.service.ApplicationService;
import com.placement.spms.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ResumeService resumeService;

    public ApplicationController(ApplicationService applicationService, ResumeService resumeService) {
        this.applicationService = applicationService;
        this.resumeService = resumeService;
    }

    @PostMapping
    public ApplicationResponse apply(@Valid @RequestBody ApplyRequest request) {
        return applicationService.apply(request);
    }

    @GetMapping("/my")
    public List<ApplicationResponse> my() {
        return applicationService.myApplications();
    }

    @GetMapping("/{id}")
    public ApplicationResponse get(@PathVariable String id) {
        return applicationService.get(id);
    }

    @GetMapping("/drive/{driveId}")
    public List<ApplicationResponse> applicants(@PathVariable String driveId) {
        return applicationService.applicants(driveId);
    }

    @PatchMapping("/{id}/status")
    public ApplicationResponse status(@PathVariable String id, @Valid @RequestBody StatusRequest request) {
        return applicationService.status(id, request);
    }

    @GetMapping("/{id}/resume")
    public ResponseEntity<Resource> resume(@PathVariable String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"resume.pdf\"")
                .body(resumeService.applicationResume(id));
    }
}
