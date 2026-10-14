package com.placement.spms.repository;

import com.placement.spms.entity.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {
    Optional<InterviewSchedule> findByApplicationId(Long applicationId);
    List<InterviewSchedule> findByApplicationStudentId(Long studentId);
    List<InterviewSchedule> findByApplicationPlacementDriveCompanyId(Long companyId);
}
