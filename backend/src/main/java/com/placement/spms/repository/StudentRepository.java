package com.placement.spms.repository;

import com.placement.spms.entity.Student;
import com.placement.spms.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByUser(User user);
    boolean existsByStudentId(String studentId);
}
