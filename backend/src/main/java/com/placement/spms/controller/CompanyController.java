package com.placement.spms.controller;

import com.placement.spms.dto.DashboardDtos.CompanyDashboard;
import com.placement.spms.dto.ProfileDtos.CompanyProfileRequest;
import com.placement.spms.dto.ProfileDtos.CompanyProfileResponse;
import com.placement.spms.service.DashboardService;
import com.placement.spms.service.DriveService;
import com.placement.spms.service.ProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies/me")
public class CompanyController {
    private final ProfileService profileService;
    private final DashboardService dashboardService;

    private final DriveService driveService;

    public CompanyController(ProfileService profileService, DashboardService dashboardService, DriveService driveService) {
        this.profileService = profileService;
        this.dashboardService = dashboardService;
        this.driveService = driveService;
    }

    @GetMapping
    public CompanyProfileResponse profile() {
        return profileService.myCompanyProfile();
    }

    @PutMapping
    public CompanyProfileResponse update(@RequestBody CompanyProfileRequest request) {
        return profileService.updateCompany(request);
    }

    @GetMapping("/drives")
    public java.util.List<com.placement.spms.dto.DriveDtos.DriveResponse> drives() {
        return driveService.myCompanyDrives();
    }

    @GetMapping("/dashboard")
    public CompanyDashboard dashboard() {
        return dashboardService.company();
    }
}
