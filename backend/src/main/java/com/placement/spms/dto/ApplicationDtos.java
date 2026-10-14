package com.placement.spms.dto;

import com.placement.spms.entity.ApplicationStatus;
import com.placement.spms.entity.InterviewMode;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;

public class ApplicationDtos {
    public record ApplyRequest(@NotNull Long placementDriveId, String coverLetter) {}
    public record StatusRequest(@NotNull ApplicationStatus status) {}
    public record ApplicationResponse(
            Long id, Long driveId, String companyName, String jobTitle, String studentName, String studentEmail,
            String studentId, String department, Instant applicationDate, ApplicationStatus status,
            String coverLetter, InterviewResponse interview
    ) {}
    public record InterviewRequest(
            @NotNull Long applicationId, String interviewRound, @NotNull LocalDateTime interviewDateTime,
            @NotNull InterviewMode mode, String locationOrMeetingDetails, String instructions
    ) {}
    public record InterviewResponse(
            Long id, Long applicationId, String companyName, String jobTitle, String studentName,
            String interviewRound, LocalDateTime interviewDateTime, InterviewMode mode,
            String locationOrMeetingDetails, String instructions
    ) {}
}
