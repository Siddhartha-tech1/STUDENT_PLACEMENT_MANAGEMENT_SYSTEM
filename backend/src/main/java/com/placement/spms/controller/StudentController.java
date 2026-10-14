package com.placement.spms.controller;

import com.placement.spms.dto.DashboardDtos.StudentDashboard;
import com.placement.spms.dto.ProfileDtos.StudentProfileRequest;
import com.placement.spms.dto.ProfileDtos.StudentProfileResponse;
import com.placement.spms.service.DashboardService;
import com.placement.spms.service.ProfileService;
import com.placement.spms.service.ResumeService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/students/me")
public class StudentController {
    private final ProfileService profileService;
    private final ResumeService resumeService;
    private final DashboardService dashboardService;

    public StudentController(ProfileService profileService, ResumeService resumeService, DashboardService dashboardService) {
        this.profileService = profileService;
        this.resumeService = resumeService;
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public StudentProfileResponse profile() {
        return profileService.myStudentProfile();
    }

    @PutMapping
    public StudentProfileResponse update(@RequestBody StudentProfileRequest request) {
        return profileService.updateStudent(request);
    }

    @PostMapping("/resume")
    public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file) {
        resumeService.upload(file);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resume")
    public ResponseEntity<Resource> resume() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"resume.pdf\"")
                .body(resumeService.myResume());
    }

    @GetMapping("/dashboard")
    public StudentDashboard dashboard() {
        return dashboardService.student();
    }
}
