package com.placement.spms.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document("applications")
@CompoundIndex(name = "student_drive_unique", def = "{'student': 1, 'placementDrive': 1}", unique = true)
public class Application {
    @Id
    private String id;

    @DBRef
    private Student student;

    @DBRef
    private PlacementDrive placementDrive;

    private Instant applicationDate = Instant.now();

    private ApplicationStatus status = ApplicationStatus.APPLIED;

    private String coverLetter;
    private Instant statusUpdatedAt = Instant.now();

    public String getId() { return id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public PlacementDrive getPlacementDrive() { return placementDrive; }
    public void setPlacementDrive(PlacementDrive placementDrive) { this.placementDrive = placementDrive; }
    public Instant getApplicationDate() { return applicationDate; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; statusUpdatedAt = Instant.now(); }
    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
    public Instant getStatusUpdatedAt() { return statusUpdatedAt; }
}
