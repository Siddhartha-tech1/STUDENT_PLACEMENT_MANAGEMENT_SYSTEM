package com.placement.spms.repository;

import com.placement.spms.entity.Company;
import com.placement.spms.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CompanyRepository extends MongoRepository<Company, String> {
    Optional<Company> findByUser(User user);
}
