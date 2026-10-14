package com.placement.spms.dto;

public class DashboardDtos {
    public record StudentDashboard(
            long availableDrives, long applicationsSubmitted, long underReview,
            long shortlisted, long upcomingInterviews, long selectedOffers
    ) {}

    public record CompanyDashboard(
            long totalDrives, long activeDrives, long totalApplications, long awaitingReview,
            long shortlisted, long interviewsScheduled, long selected
    ) {}

    public record AdminDashboard(
            long totalStudents, long totalCompanies, long totalPlacementDrives, long openPlacementDrives,
            long totalApplications, long studentsShortlisted, long studentsSelected, double placementPercentage
    ) {}
}
