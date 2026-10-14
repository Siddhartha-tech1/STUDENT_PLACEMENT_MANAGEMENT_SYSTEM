package com.placement.spms.controller;

import com.placement.spms.dto.DashboardDtos.AdminDashboard;
import com.placement.spms.dto.AdminDtos.UserResponse;
import com.placement.spms.dto.ApplicationDtos.ApplicationResponse;
import com.placement.spms.dto.DriveDtos.DriveResponse;
import com.placement.spms.dto.ProfileDtos.CompanyProfileResponse;
import com.placement.spms.dto.ProfileDtos.StudentProfileResponse;
import com.placement.spms.entity.AccountStatus;
import com.placement.spms.entity.User;
import com.placement.spms.repository.*;
import com.placement.spms.service.ApplicationService;
import com.placement.spms.service.DashboardService;
import com.placement.spms.service.DriveService;
import com.placement.spms.service.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final DashboardService dashboardService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final PlacementDriveRepository driveRepository;
    private final ApplicationRepository applicationRepository;
    private final ProfileService profileService;
    private final DriveService driveService;
    private final ApplicationService applicationService;

    public AdminController(DashboardService dashboardService, UserRepository userRepository, StudentRepository studentRepository,
                           CompanyRepository companyRepository, PlacementDriveRepository driveRepository,
                           ApplicationRepository applicationRepository, ProfileService profileService,
                           DriveService driveService, ApplicationService applicationService) {
        this.dashboardService = dashboardService;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.driveRepository = driveRepository;
        this.applicationRepository = applicationRepository;
        this.profileService = profileService;
        this.driveService = driveService;
        this.applicationService = applicationService;
    }

    @GetMapping("/dashboard")
    public AdminDashboard dashboard() {
        return dashboardService.admin();
    }

    @GetMapping("/users")
    public List<UserResponse> users() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole(), user.getStatus()))
                .toList();
    }

    @PatchMapping("/users/{id}/status")
    public User status(@PathVariable String id, @RequestParam AccountStatus status) {
        User user = userRepository.findById(id).orElseThrow();
        user.setStatus(status);
        return userRepository.save(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/students")
    public List<StudentProfileResponse> students() {
        return studentRepository.findAll().stream().map(profileService::toStudentResponse).toList();
    }

    @GetMapping("/companies")
    public List<CompanyProfileResponse> companies() {
        return companyRepository.findAll().stream().map(profileService::toCompanyResponse).toList();
    }

    @GetMapping("/placement-drives")
    public List<DriveResponse> drives() {
        return driveRepository.findAll().stream().map(driveService::toResponse).toList();
    }

    @GetMapping("/applications")
    public List<ApplicationResponse> applications() {
        return applicationRepository.findAll().stream().map(applicationService::toResponse).toList();
    }

    @GetMapping("/placement-statistics")
    public AdminDashboard statistics() {
        return dashboardService.admin();
    }
}
