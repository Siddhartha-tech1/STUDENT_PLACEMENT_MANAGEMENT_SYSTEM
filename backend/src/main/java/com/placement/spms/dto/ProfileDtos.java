package com.placement.spms.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class ProfileDtos {
    public record StudentProfileResponse(
            Long id, String fullName, String email, String studentId, String phone, String college,
            String university, String department, String degree, Integer graduationYear, BigDecimal cgpa,
            Integer backlogs, String skills, String certifications, String projects, boolean resumeUploaded,
            String resumeFileName, Instant resumeUploadedAt
    ) {}

    public record StudentProfileRequest(
            String fullName, String phone, String college, String university, String department, String degree,
            Integer graduationYear, BigDecimal cgpa, Integer backlogs, String skills, String certifications,
            String projects
    ) {}

    public record CompanyProfileResponse(
            Long id, String fullName, String email, String companyName, String website, String industry,
            String description, String contactPerson, String contactNumber, String location, boolean verified
    ) {}

    public record CompanyProfileRequest(
            String fullName, String companyName, String website, String industry, String description,
            String contactPerson, String contactNumber, String location
    ) {}
}
