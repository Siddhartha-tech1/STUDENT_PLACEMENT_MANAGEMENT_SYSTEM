package com.placement.spms.service;

import com.placement.spms.dto.ApplicationDtos.*;
import com.placement.spms.dto.DriveDtos.EligibilityResponse;
import com.placement.spms.entity.*;
import com.placement.spms.exception.ApiException;
import com.placement.spms.repository.ApplicationRepository;
import com.placement.spms.repository.InterviewScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final InterviewScheduleRepository interviewRepository;
    private final ProfileService profileService;
    private final CurrentUserService currentUserService;
    private final DriveService driveService;

    public ApplicationService(ApplicationRepository applicationRepository, InterviewScheduleRepository interviewRepository,
                              ProfileService profileService, CurrentUserService currentUserService, DriveService driveService) {
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.profileService = profileService;
        this.currentUserService = currentUserService;
        this.driveService = driveService;
    }

    @Transactional
    public ApplicationResponse apply(ApplyRequest request) {
        Student student = profileService.student();
        PlacementDrive drive = driveService.find(request.placementDriveId());
        EligibilityResponse eligibility = driveService.eligibility(student, drive);
        if (!eligibility.eligible()) throw new ApiException(HttpStatus.BAD_REQUEST, eligibility.reason());
        if (applicationRepository.existsByStudentAndPlacementDrive(student, drive)) {
            throw new ApiException(HttpStatus.CONFLICT, "You already applied for this drive");
        }
        Application application = new Application();
        application.setStudent(student);
        application.setPlacementDrive(drive);
        application.setCoverLetter(request.coverLetter());
        return toResponse(applicationRepository.save(application));
    }

    public List<ApplicationResponse> myApplications() {
        return applicationRepository.findByStudentOrderByApplicationDateDesc(profileService.student()).stream().map(this::toResponse).toList();
    }

    public List<ApplicationResponse> applicants(Long driveId) {
        PlacementDrive drive = driveService.find(driveId);
        if (!drive.getCompany().getUser().getId().equals(currentUserService.user().getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You can view applicants only for your drives");
        }
        return applicationRepository.findByPlacementDriveOrderByApplicationDateDesc(drive).stream().map(this::toResponse).toList();
    }

    public ApplicationResponse get(Long id) {
        return toResponse(authorized(id));
    }

    @Transactional
    public ApplicationResponse status(Long id, StatusRequest request) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Application not found"));
        User user = currentUserService.user();
        boolean companyOwner = application.getPlacementDrive().getCompany().getUser().getId().equals(user.getId());
        if (user.getRole() != Role.ADMIN && !companyOwner) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Students cannot update application status");
        }
        application.setStatus(request.status());
        return toResponse(applicationRepository.save(application));
    }

    public Application authorized(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Application not found"));
        User user = currentUserService.user();
        boolean allowed = user.getRole() == Role.ADMIN
                || application.getStudent().getUser().getId().equals(user.getId())
                || application.getPlacementDrive().getCompany().getUser().getId().equals(user.getId());
        if (!allowed) throw new ApiException(HttpStatus.FORBIDDEN, "You cannot access this application");
        return application;
    }

    public ApplicationResponse toResponse(Application application) {
        InterviewResponse interview = interviewRepository.findByApplicationId(application.getId())
                .map(schedule -> new InterviewResponse(schedule.getId(), application.getId(),
                        application.getPlacementDrive().getCompany().getCompanyName(), application.getPlacementDrive().getJobTitle(),
                        application.getStudent().getUser().getFullName(), schedule.getInterviewRound(),
                        schedule.getInterviewDateTime(), schedule.getMode(), schedule.getLocationOrMeetingDetails(),
                        schedule.getInstructions()))
                .orElse(null);
        return new ApplicationResponse(application.getId(), application.getPlacementDrive().getId(),
                application.getPlacementDrive().getCompany().getCompanyName(), application.getPlacementDrive().getJobTitle(),
                application.getStudent().getUser().getFullName(), application.getStudent().getUser().getEmail(),
                application.getStudent().getStudentId(), application.getStudent().getDepartment(), application.getApplicationDate(),
                application.getStatus(), application.getCoverLetter(), interview);
    }
}
