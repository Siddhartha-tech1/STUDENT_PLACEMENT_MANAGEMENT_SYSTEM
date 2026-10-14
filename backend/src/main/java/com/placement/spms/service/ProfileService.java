package com.placement.spms.service;

import com.placement.spms.dto.ProfileDtos.*;
import com.placement.spms.entity.Company;
import com.placement.spms.entity.Student;
import com.placement.spms.entity.User;
import com.placement.spms.exception.ApiException;
import com.placement.spms.repository.CompanyRepository;
import com.placement.spms.repository.StudentRepository;
import com.placement.spms.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {
    private final CurrentUserService currentUserService;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public ProfileService(CurrentUserService currentUserService, StudentRepository studentRepository, CompanyRepository companyRepository,
                          UserRepository userRepository) {
        this.currentUserService = currentUserService;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Student student() {
        return studentRepository.findByUser(currentUserService.user())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student profile not found"));
    }

    public Company company() {
        return companyRepository.findByUser(currentUserService.user())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Company profile not found"));
    }

    public StudentProfileResponse myStudentProfile() {
        return toStudentResponse(student());
    }

    @Transactional
    public StudentProfileResponse updateStudent(StudentProfileRequest request) {
        Student student = student();
        User user = student.getUser();
        if (request.fullName() != null) user.setFullName(request.fullName());
        if (request.phone() != null) student.setPhone(request.phone());
        if (request.college() != null) student.setCollege(request.college());
        if (request.university() != null) student.setUniversity(request.university());
        if (request.department() != null) student.setDepartment(request.department());
        if (request.degree() != null) student.setDegree(request.degree());
        if (request.graduationYear() != null) student.setGraduationYear(request.graduationYear());
        if (request.cgpa() != null) student.setCgpa(request.cgpa());
        if (request.backlogs() != null) student.setBacklogs(request.backlogs());
        if (request.skills() != null) student.setSkills(request.skills());
        if (request.certifications() != null) student.setCertifications(request.certifications());
        if (request.projects() != null) student.setProjects(request.projects());
        userRepository.save(user);
        return toStudentResponse(studentRepository.save(student));
    }

    public CompanyProfileResponse myCompanyProfile() {
        return toCompanyResponse(company());
    }

    @Transactional
    public CompanyProfileResponse updateCompany(CompanyProfileRequest request) {
        Company company = company();
        if (request.fullName() != null) company.getUser().setFullName(request.fullName());
        if (request.companyName() != null) company.setCompanyName(request.companyName());
        if (request.website() != null) company.setWebsite(request.website());
        if (request.industry() != null) company.setIndustry(request.industry());
        if (request.description() != null) company.setDescription(request.description());
        if (request.contactPerson() != null) company.setContactPerson(request.contactPerson());
        if (request.contactNumber() != null) company.setContactNumber(request.contactNumber());
        if (request.location() != null) company.setLocation(request.location());
        userRepository.save(company.getUser());
        return toCompanyResponse(companyRepository.save(company));
    }

    public StudentProfileResponse toStudentResponse(Student student) {
        return new StudentProfileResponse(student.getId(), student.getUser().getFullName(), student.getUser().getEmail(),
                student.getStudentId(), student.getPhone(), student.getCollege(), student.getUniversity(),
                student.getDepartment(), student.getDegree(), student.getGraduationYear(), student.getCgpa(),
                student.getBacklogs(), student.getSkills(), student.getCertifications(), student.getProjects(),
                student.getResumeFileName() != null, student.getResumeFileName(), student.getResumeUploadedAt());
    }

    public CompanyProfileResponse toCompanyResponse(Company company) {
        return new CompanyProfileResponse(company.getId(), company.getUser().getFullName(), company.getUser().getEmail(),
                company.getCompanyName(), company.getWebsite(), company.getIndustry(), company.getDescription(),
                company.getContactPerson(), company.getContactNumber(), company.getLocation(), company.isVerified());
    }
}
