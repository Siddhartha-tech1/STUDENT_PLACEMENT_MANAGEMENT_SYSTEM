package com.placement.spms.service;

import com.placement.spms.dto.AuthDtos.AuthResponse;
import com.placement.spms.dto.AuthDtos.LoginRequest;
import com.placement.spms.dto.AuthDtos.RegisterRequest;
import com.placement.spms.entity.*;
import com.placement.spms.exception.ApiException;
import com.placement.spms.repository.CompanyRepository;
import com.placement.spms.repository.StudentRepository;
import com.placement.spms.repository.UserRepository;
import com.placement.spms.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, StudentRepository studentRepository,
                       CompanyRepository companyRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (request.role() == Role.ADMIN) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Public registration cannot create administrator accounts");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email is already registered");
        }
        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        if (request.role() == Role.STUDENT) {
            if (!StringUtils.hasText(request.studentId()) || !StringUtils.hasText(request.department()) || request.graduationYear() == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Student ID, department, and graduation year are required");
            }
            if (studentRepository.existsByStudentId(request.studentId())) {
                throw new ApiException(HttpStatus.CONFLICT, "Student ID is already registered");
            }
            Student student = new Student();
            student.setUser(user);
            student.setStudentId(request.studentId());
            student.setDepartment(request.department());
            student.setGraduationYear(request.graduationYear());
            studentRepository.save(student);
        } else {
            if (!StringUtils.hasText(request.companyName())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Company name is required");
            }
            Company company = new Company();
            company.setUser(user);
            company.setCompanyName(request.companyName());
            company.setContactPerson(request.contactPerson());
            company.setContactNumber(request.contactNumber());
            companyRepository.save(company);
        }
        return authResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (user.getStatus() != AccountStatus.ACTIVE || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return authResponse(user);
    }

    private AuthResponse authResponse(User user) {
        return new AuthResponse(jwtService.generate(user), user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }
}
