package com.placement.spms.service;

import com.placement.spms.entity.Application;
import com.placement.spms.entity.Role;
import com.placement.spms.entity.Student;
import com.placement.spms.entity.User;
import com.placement.spms.exception.ApiException;
import com.placement.spms.repository.ApplicationRepository;
import com.placement.spms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

@Service
public class ResumeService {
    private final ProfileService profileService;
    private final CurrentUserService currentUserService;
    private final StudentRepository studentRepository;
    private final ApplicationRepository applicationRepository;
    private final Path storageDir;

    public ResumeService(ProfileService profileService, CurrentUserService currentUserService,
                         StudentRepository studentRepository, ApplicationRepository applicationRepository,
                         @Value("${app.resume.storage-dir}") String storageDir) {
        this.profileService = profileService;
        this.currentUserService = currentUserService;
        this.studentRepository = studentRepository;
        this.applicationRepository = applicationRepository;
        this.storageDir = Path.of(storageDir).toAbsolutePath().normalize();
    }

    @Transactional
    public void upload(MultipartFile file) {
        validatePdf(file);
        Student student = profileService.student();
        try {
            Files.createDirectories(storageDir);
            String filename = "student-" + student.getId() + "-" + UUID.randomUUID() + ".pdf";
            file.transferTo(storageDir.resolve(filename));
            student.setResumeFileName(filename);
            student.setResumeContentType("application/pdf");
            student.setResumeSize(file.getSize());
            student.setResumeUploadedAt(Instant.now());
            studentRepository.save(student);
        } catch (IOException ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store resume");
        }
    }

    public Resource myResume() {
        return resumeForStudent(profileService.student());
    }

    public Resource applicationResume(String applicationId) {
        User user = currentUserService.user();
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Application not found"));
        boolean allowed = user.getRole() == Role.ADMIN
                || application.getStudent().getUser().getId().equals(user.getId())
                || application.getPlacementDrive().getCompany().getUser().getId().equals(user.getId());
        if (!allowed) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You cannot access this resume");
        }
        return resumeForStudent(application.getStudent());
    }

    private Resource resumeForStudent(Student student) {
        if (student.getResumeFileName() == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Resume not uploaded");
        }
        try {
            Path path = storageDir.resolve(student.getResumeFileName()).normalize();
            if (!path.startsWith(storageDir) || !Files.exists(path)) {
                throw new ApiException(HttpStatus.NOT_FOUND, "Resume file is missing");
            }
            return new UrlResource(path.toUri());
        } catch (IOException ex) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Resume file is missing");
        }
    }

    private void validatePdf(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Resume PDF is required");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Resume must be 5 MB or smaller");
        }
        try (InputStream in = file.getInputStream()) {
            byte[] header = in.readNBytes(4);
            if (header.length < 4 || header[0] != '%' || header[1] != 'P' || header[2] != 'D' || header[3] != 'F') {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Only valid PDF files are allowed");
            }
        } catch (IOException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Could not read uploaded file");
        }
    }
}
