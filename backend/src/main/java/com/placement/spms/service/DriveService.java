package com.placement.spms.service;

import com.placement.spms.dto.DriveDtos.*;
import com.placement.spms.entity.*;
import com.placement.spms.exception.ApiException;
import com.placement.spms.repository.ApplicationRepository;
import com.placement.spms.repository.PlacementDriveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class DriveService {
    private final PlacementDriveRepository driveRepository;
    private final ApplicationRepository applicationRepository;
    private final ProfileService profileService;
    private final CurrentUserService currentUserService;

    public DriveService(PlacementDriveRepository driveRepository, ApplicationRepository applicationRepository,
                        ProfileService profileService, CurrentUserService currentUserService) {
        this.driveRepository = driveRepository;
        this.applicationRepository = applicationRepository;
        this.profileService = profileService;
        this.currentUserService = currentUserService;
    }

    public Page<DriveResponse> search(String keyword, DriveStatus status, Pageable pageable) {
        return driveRepository.search(blankToNull(keyword), status, pageable).map(this::toResponse);
    }

    public List<DriveResponse> myCompanyDrives() {
        return driveRepository.findByCompanyOrderByCreatedAtDesc(profileService.company())
                .stream().map(this::toResponse).toList();
    }

    public DriveResponse get(Long id) {
        return toResponse(find(id));
    }

    @Transactional
    public DriveResponse create(DriveRequest request) {
        PlacementDrive drive = new PlacementDrive();
        drive.setCompany(profileService.company());
        apply(request, drive);
        return toResponse(driveRepository.save(drive));
    }

    @Transactional
    public DriveResponse update(Long id, DriveRequest request) {
        PlacementDrive drive = ownedDrive(id);
        apply(request, drive);
        return toResponse(driveRepository.save(drive));
    }

    @Transactional
    public void delete(Long id) {
        driveRepository.delete(ownedDrive(id));
    }

    @Transactional
    public DriveResponse updateStatus(Long id, DriveStatus status) {
        PlacementDrive drive = currentUserService.user().getRole() == Role.ADMIN ? find(id) : ownedDrive(id);
        drive.setStatus(status);
        return toResponse(driveRepository.save(drive));
    }

    public EligibilityResponse eligibility(Long id) {
        return eligibility(profileService.student(), find(id));
    }

    public EligibilityResponse eligibility(Student student, PlacementDrive drive) {
        if (drive.getStatus() != DriveStatus.OPEN) return new EligibilityResponse(false, "Placement drive is not open");
        if (drive.getApplicationDeadline().isBefore(LocalDate.now())) return new EligibilityResponse(false, "Application deadline has passed");
        if (drive.getMinimumCgpa() != null && student.getCgpa() != null && student.getCgpa().compareTo(drive.getMinimumCgpa()) < 0) {
            return new EligibilityResponse(false, "CGPA below minimum requirement");
        }
        if (drive.getMinimumCgpa() != null && student.getCgpa() == null) return new EligibilityResponse(false, "CGPA is missing from profile");
        if (!containsIgnoreCase(drive.getEligibleDepartments(), student.getDepartment())) return new EligibilityResponse(false, "Department is not eligible");
        if (!containsIgnoreCase(drive.getEligibleGraduationYears(), String.valueOf(student.getGraduationYear()))) {
            return new EligibilityResponse(false, "Graduation year is not eligible");
        }
        if (student.getBacklogs() != null && drive.getMaxBacklogs() != null && student.getBacklogs() > drive.getMaxBacklogs()) {
            return new EligibilityResponse(false, "Backlogs exceed permitted limit");
        }
        return new EligibilityResponse(true, "Eligible to apply");
    }

    public PlacementDrive find(Long id) {
        return driveRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Placement drive not found"));
    }

    private PlacementDrive ownedDrive(Long id) {
        PlacementDrive drive = find(id);
        if (!drive.getCompany().getUser().getId().equals(currentUserService.user().getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You can manage only your company's drives");
        }
        return drive;
    }

    private void apply(DriveRequest request, PlacementDrive drive) {
        if (request.applicationDeadline().isBefore(LocalDate.now())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Application deadline cannot be in the past");
        }
        drive.setJobTitle(request.jobTitle());
        drive.setJobDescription(request.jobDescription());
        drive.setRequiredQualifications(request.requiredQualifications());
        drive.setLocation(request.location());
        drive.setEmploymentType(request.employmentType());
        drive.setSalaryPackage(request.salaryPackage());
        drive.setMinimumCgpa(request.minimumCgpa());
        drive.setEligibleDepartments(request.eligibleDepartments());
        drive.setEligibleGraduationYears(request.eligibleGraduationYears());
        drive.setMaxBacklogs(request.maxBacklogs() == null ? 0 : request.maxBacklogs());
        drive.setRequiredSkills(request.requiredSkills());
        drive.setApplicationOpenDate(request.applicationOpenDate());
        drive.setApplicationDeadline(request.applicationDeadline());
        drive.setRecruitmentProcess(request.recruitmentProcess());
        drive.setStatus(request.status() == null ? DriveStatus.OPEN : request.status());
    }

    public DriveResponse toResponse(PlacementDrive drive) {
        return new DriveResponse(drive.getId(), drive.getCompany().getCompanyName(), drive.getCompany().getId(),
                drive.getJobTitle(), drive.getJobDescription(), drive.getRequiredQualifications(), drive.getLocation(),
                drive.getEmploymentType(), drive.getSalaryPackage(), drive.getMinimumCgpa(), drive.getEligibleDepartments(),
                drive.getEligibleGraduationYears(), drive.getMaxBacklogs(), drive.getRequiredSkills(),
                drive.getApplicationOpenDate(), drive.getApplicationDeadline(), drive.getRecruitmentProcess(),
                drive.getStatus(), applicationRepository.findByPlacementDriveOrderByApplicationDateDesc(drive).size());
    }

    private boolean containsIgnoreCase(String csv, String value) {
        if (csv == null || csv.isBlank()) return true;
        if (value == null) return false;
        return Arrays.stream(csv.split(",")).map(String::trim).anyMatch(item -> item.equalsIgnoreCase(value));
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
