package com.placement.spms.service;

import com.placement.spms.dto.DashboardDtos.*;
import com.placement.spms.entity.ApplicationStatus;
import com.placement.spms.entity.Company;
import com.placement.spms.entity.DriveStatus;
import com.placement.spms.entity.Role;
import com.placement.spms.entity.Student;
import com.placement.spms.repository.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final PlacementDriveRepository driveRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewScheduleRepository interviewRepository;
    private final UserRepository userRepository;
    private final ProfileService profileService;

    public DashboardService(PlacementDriveRepository driveRepository, ApplicationRepository applicationRepository,
                            InterviewScheduleRepository interviewRepository, UserRepository userRepository,
                            ProfileService profileService) {
        this.driveRepository = driveRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.userRepository = userRepository;
        this.profileService = profileService;
    }

    public StudentDashboard student() {
        Student student = profileService.student();
        return new StudentDashboard(
                driveRepository.countByStatus(DriveStatus.OPEN),
                applicationRepository.countByStudent(student),
                applicationRepository.countByStudentAndStatus(student, ApplicationStatus.UNDER_REVIEW),
                applicationRepository.countByStudentAndStatus(student, ApplicationStatus.SHORTLISTED),
                interviewRepository.findAll().stream().filter(interview -> interview.getApplication().getStudent().getId().equals(student.getId())).count(),
                applicationRepository.countByStudentAndStatus(student, ApplicationStatus.SELECTED)
        );
    }

    public CompanyDashboard company() {
        Company company = profileService.company();
        return new CompanyDashboard(
                driveRepository.countByCompany(company),
                driveRepository.countByCompanyAndStatus(company, DriveStatus.OPEN),
                applicationsFor(company).count(),
                applicationsFor(company).filter(application -> application.getStatus() == ApplicationStatus.APPLIED).count(),
                applicationsFor(company).filter(application -> application.getStatus() == ApplicationStatus.SHORTLISTED).count(),
                applicationsFor(company).filter(application -> application.getStatus() == ApplicationStatus.INTERVIEW_SCHEDULED).count(),
                applicationsFor(company).filter(application -> application.getStatus() == ApplicationStatus.SELECTED).count()
        );
    }

    public AdminDashboard admin() {
        long students = userRepository.countByRole(Role.STUDENT);
        long selected = applicationRepository.countByStatus(ApplicationStatus.SELECTED);
        double percentage = students == 0 ? 0 : (selected * 100.0) / students;
        return new AdminDashboard(students, userRepository.countByRole(Role.COMPANY), driveRepository.count(),
                driveRepository.countByStatus(DriveStatus.OPEN), applicationRepository.count(),
                applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED), selected, percentage);
    }

    private java.util.stream.Stream<com.placement.spms.entity.Application> applicationsFor(Company company) {
        return applicationRepository.findAll().stream()
                .filter(application -> application.getPlacementDrive().getCompany().getId().equals(company.getId()));
    }
}
