package com.placement.spms.repository;

import com.placement.spms.entity.InterviewSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InterviewScheduleRepository extends MongoRepository<InterviewSchedule, String> {
    Optional<InterviewSchedule> findByApplication(com.placement.spms.entity.Application application);
}
