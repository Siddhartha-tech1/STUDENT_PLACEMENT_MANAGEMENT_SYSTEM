package com.placement.spms.repository;

import com.placement.spms.entity.Application;
import com.placement.spms.entity.ApplicationStatus;
import com.placement.spms.entity.PlacementDrive;
import com.placement.spms.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    boolean existsByStudentAndPlacementDrive(Student student, PlacementDrive placementDrive);
    List<Application> findByStudentOrderByApplicationDateDesc(Student student);
    List<Application> findByPlacementDriveOrderByApplicationDateDesc(PlacementDrive placementDrive);
    long countByStudent(Student student);
    long countByStudentAndStatus(Student student, ApplicationStatus status);
    long countByStatus(ApplicationStatus status);
}
