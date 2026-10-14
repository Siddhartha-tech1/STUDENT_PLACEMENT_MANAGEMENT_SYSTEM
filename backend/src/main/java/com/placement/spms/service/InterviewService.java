package com.placement.spms.service;

import com.placement.spms.dto.ApplicationDtos.InterviewRequest;
import com.placement.spms.dto.ApplicationDtos.InterviewResponse;
import com.placement.spms.entity.*;
import com.placement.spms.exception.ApiException;
import com.placement.spms.repository.ApplicationRepository;
import com.placement.spms.repository.InterviewScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InterviewService {
    private final InterviewScheduleRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final CurrentUserService currentUserService;
    private final ProfileService profileService;

    public InterviewService(InterviewScheduleRepository interviewRepository, ApplicationRepository applicationRepository,
                            CurrentUserService currentUserService, ProfileService profileService) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
        this.currentUserService = currentUserService;
        this.profileService = profileService;
    }

    @Transactional
    public InterviewResponse save(InterviewRequest request) {
        Application application = applicationRepository.findById(request.applicationId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Application not found"));
        if (!application.getPlacementDrive().getCompany().getUser().getId().equals(currentUserService.user().getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You can schedule interviews only for your applicants");
        }
        InterviewSchedule schedule = interviewRepository.findByApplication(application).orElseGet(InterviewSchedule::new);
        schedule.setApplication(application);
        schedule.setInterviewRound(request.interviewRound());
        schedule.setInterviewDateTime(request.interviewDateTime());
        schedule.setMode(request.mode());
        schedule.setLocationOrMeetingDetails(request.locationOrMeetingDetails());
        schedule.setInstructions(request.instructions());
        application.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        applicationRepository.save(application);
        return toResponse(interviewRepository.save(schedule));
    }

    public List<InterviewResponse> my() {
        User user = currentUserService.user();
        List<InterviewSchedule> schedules = user.getRole() == Role.STUDENT
                ? interviewRepository.findAll().stream().filter(schedule -> schedule.getApplication().getStudent().getId().equals(profileService.student().getId())).toList()
                : interviewRepository.findAll().stream().filter(schedule -> schedule.getApplication().getPlacementDrive().getCompany().getId().equals(profileService.company().getId())).toList();
        return schedules.stream().map(this::toResponse).toList();
    }

    @Transactional
    public void delete(String id) {
        InterviewSchedule schedule = interviewRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Interview not found"));
        if (!schedule.getApplication().getPlacementDrive().getCompany().getUser().getId().equals(currentUserService.user().getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You cannot cancel this interview");
        }
        interviewRepository.delete(schedule);
    }

    private InterviewResponse toResponse(InterviewSchedule schedule) {
        Application application = schedule.getApplication();
        return new InterviewResponse(schedule.getId(), application.getId(),
                application.getPlacementDrive().getCompany().getCompanyName(), application.getPlacementDrive().getJobTitle(),
                application.getStudent().getUser().getFullName(), schedule.getInterviewRound(),
                schedule.getInterviewDateTime(), schedule.getMode(), schedule.getLocationOrMeetingDetails(),
                schedule.getInstructions());
    }
}
