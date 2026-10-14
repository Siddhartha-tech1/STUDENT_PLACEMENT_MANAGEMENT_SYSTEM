package com.placement.spms.repository;

import com.placement.spms.entity.Company;
import com.placement.spms.entity.DriveStatus;
import com.placement.spms.entity.PlacementDrive;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlacementDriveRepository extends MongoRepository<PlacementDrive, String> {
    List<PlacementDrive> findTop5ByStatusOrderByCreatedAtDesc(DriveStatus status);
    List<PlacementDrive> findByCompanyOrderByCreatedAtDesc(Company company);
    long countByStatus(DriveStatus status);
    long countByCompany(Company company);
    long countByCompanyAndStatus(Company company, DriveStatus status);
}
