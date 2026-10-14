package com.placement.spms.repository;

import com.placement.spms.entity.Company;
import com.placement.spms.entity.DriveStatus;
import com.placement.spms.entity.PlacementDrive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlacementDriveRepository extends JpaRepository<PlacementDrive, Long> {
    List<PlacementDrive> findTop5ByStatusOrderByCreatedAtDesc(DriveStatus status);
    List<PlacementDrive> findByCompanyOrderByCreatedAtDesc(Company company);
    long countByStatus(DriveStatus status);
    long countByCompany(Company company);
    long countByCompanyAndStatus(Company company, DriveStatus status);

    @Query("""
            select d from PlacementDrive d
            where (:status is null or d.status = :status)
            and (:keyword is null or lower(d.jobTitle) like lower(concat('%', :keyword, '%'))
                 or lower(d.company.companyName) like lower(concat('%', :keyword, '%'))
                 or lower(d.location) like lower(concat('%', :keyword, '%'))
                 or lower(d.requiredSkills) like lower(concat('%', :keyword, '%')))
            """)
    Page<PlacementDrive> search(@Param("keyword") String keyword, @Param("status") DriveStatus status, Pageable pageable);
}
