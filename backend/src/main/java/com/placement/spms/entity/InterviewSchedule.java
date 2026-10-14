package com.placement.spms.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.time.LocalDateTime;

@Document("interview_schedules")
public class InterviewSchedule {
    @Id
    private String id;

    @DBRef
    private Application application;

    private String interviewRound;
    private LocalDateTime interviewDateTime;
    private InterviewMode mode;
    private String locationOrMeetingDetails;
    private String instructions;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public String getId() { return id; }
    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }
    public String getInterviewRound() { return interviewRound; }
    public void setInterviewRound(String interviewRound) { this.interviewRound = interviewRound; }
    public LocalDateTime getInterviewDateTime() { return interviewDateTime; }
    public void setInterviewDateTime(LocalDateTime interviewDateTime) { this.interviewDateTime = interviewDateTime; }
    public InterviewMode getMode() { return mode; }
    public void setMode(InterviewMode mode) { this.mode = mode; }
    public String getLocationOrMeetingDetails() { return locationOrMeetingDetails; }
    public void setLocationOrMeetingDetails(String locationOrMeetingDetails) { this.locationOrMeetingDetails = locationOrMeetingDetails; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
