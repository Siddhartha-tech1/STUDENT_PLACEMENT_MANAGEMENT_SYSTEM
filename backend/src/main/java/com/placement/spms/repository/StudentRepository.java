package com.placement.spms.repository;

import com.placement.spms.entity.Student;
import com.placement.spms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
    boolean existsByStudentId(String studentId);
}
