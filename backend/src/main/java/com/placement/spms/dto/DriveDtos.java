package com.placement.spms.dto;

import com.placement.spms.entity.DriveStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DriveDtos {
    public record DriveRequest(
            @NotBlank String jobTitle,
            @NotBlank String jobDescription,
            String requiredQualifications,
            String location,
            String employmentType,
            BigDecimal salaryPackage,
            BigDecimal minimumCgpa,
            String eligibleDepartments,
            String eligibleGraduationYears,
            Integer maxBacklogs,
            String requiredSkills,
            LocalDate applicationOpenDate,
            @NotNull LocalDate applicationDeadline,
            String recruitmentProcess,
            DriveStatus status
    ) {}

    public record DriveResponse(
            Long id, String companyName, Long companyId, String jobTitle, String jobDescription,
            String requiredQualifications, String location, String employmentType, BigDecimal salaryPackage,
            BigDecimal minimumCgpa, String eligibleDepartments, String eligibleGraduationYears,
            Integer maxBacklogs, String requiredSkills, LocalDate applicationOpenDate,
            LocalDate applicationDeadline, String recruitmentProcess, DriveStatus status, long applicantCount
    ) {}

    public record EligibilityResponse(boolean eligible, String reason) {}
}
