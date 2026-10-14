package com.placement.spms.dto;

import com.placement.spms.entity.ApplicationStatus;
import com.placement.spms.entity.InterviewMode;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;

public class ApplicationDtos {
    public record ApplyRequest(@NotNull String placementDriveId, String coverLetter) {}
    public record StatusRequest(@NotNull ApplicationStatus status) {}
    public record ApplicationResponse(
            String id, String driveId, String companyName, String jobTitle, String studentName, String studentEmail,
            String studentId, String department, Instant applicationDate, ApplicationStatus status,
            String coverLetter, InterviewResponse interview
    ) {}
    public record InterviewRequest(
            @NotNull String applicationId, String interviewRound, @NotNull LocalDateTime interviewDateTime,
            @NotNull InterviewMode mode, String locationOrMeetingDetails, String instructions
    ) {}
    public record InterviewResponse(
            String id, String applicationId, String companyName, String jobTitle, String studentName,
            String interviewRound, LocalDateTime interviewDateTime, InterviewMode mode,
            String locationOrMeetingDetails, String instructions
    ) {}
}
