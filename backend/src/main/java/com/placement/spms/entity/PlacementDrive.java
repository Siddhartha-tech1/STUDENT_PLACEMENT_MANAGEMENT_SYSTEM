package com.placement.spms.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Document("placement_drives")
public class PlacementDrive {
    @Id
    private String id;

    @DBRef
    private Company company;

    private String jobTitle;
    private String jobDescription;
    private String requiredQualifications;
    private String location;
    private String employmentType;
    private BigDecimal salaryPackage;
    private BigDecimal minimumCgpa;
    private String eligibleDepartments;
    private String eligibleGraduationYears;
    private Integer maxBacklogs = 0;
    private String requiredSkills;
    private LocalDate applicationOpenDate;
    private LocalDate applicationDeadline;
    private String recruitmentProcess;
    private DriveStatus status = DriveStatus.OPEN;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public String getId() { return id; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
    public String getRequiredQualifications() { return requiredQualifications; }
    public void setRequiredQualifications(String requiredQualifications) { this.requiredQualifications = requiredQualifications; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
    public BigDecimal getSalaryPackage() { return salaryPackage; }
    public void setSalaryPackage(BigDecimal salaryPackage) { this.salaryPackage = salaryPackage; }
    public BigDecimal getMinimumCgpa() { return minimumCgpa; }
    public void setMinimumCgpa(BigDecimal minimumCgpa) { this.minimumCgpa = minimumCgpa; }
    public String getEligibleDepartments() { return eligibleDepartments; }
    public void setEligibleDepartments(String eligibleDepartments) { this.eligibleDepartments = eligibleDepartments; }
    public String getEligibleGraduationYears() { return eligibleGraduationYears; }
    public void setEligibleGraduationYears(String eligibleGraduationYears) { this.eligibleGraduationYears = eligibleGraduationYears; }
    public Integer getMaxBacklogs() { return maxBacklogs; }
    public void setMaxBacklogs(Integer maxBacklogs) { this.maxBacklogs = maxBacklogs; }
    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }
    public LocalDate getApplicationOpenDate() { return applicationOpenDate; }
    public void setApplicationOpenDate(LocalDate applicationOpenDate) { this.applicationOpenDate = applicationOpenDate; }
    public LocalDate getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(LocalDate applicationDeadline) { this.applicationDeadline = applicationDeadline; }
    public String getRecruitmentProcess() { return recruitmentProcess; }
    public void setRecruitmentProcess(String recruitmentProcess) { this.recruitmentProcess = recruitmentProcess; }
    public DriveStatus getStatus() { return status; }
    public void setStatus(DriveStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
