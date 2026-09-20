package com.ecole.service;

import com.ecole.dto.TeacherRequest;
import com.ecole.dto.TeacherResponse;
import com.ecole.exception.ResourceNotFoundException;
import com.ecole.model.School;
import com.ecole.model.Teacher;
import com.ecole.repository.SchoolRepository;
import com.ecole.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;
    private final EmailService emailService;

    public TeacherResponse createTeacher(TeacherRequest request) {
        if (teacherRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Teacher with email already exists");
        }
        if (teacherRepository.existsByNationalId(request.getNationalId())) {
            throw new RuntimeException("Teacher with national ID already exists");
        }

        School school = schoolRepository.findById(request.getSchoolId())
            .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + request.getSchoolId()));

        Teacher teacher = Teacher.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .dateOfBirth(request.getDateOfBirth())
            .gender(request.getGender())
            .address(request.getAddress())
            .city(request.getCity())
            .postalCode(request.getPostalCode())
            .country(request.getCountry())
            .nationalId(request.getNationalId())
            .qualification(request.getQualification())
            .specialization(request.getSpecialization())
            .yearsOfExperience(request.getYearsOfExperience())
            .emergencyContactName(request.getEmergencyContactName())
            .emergencyContactPhone(request.getEmergencyContactPhone())
            .emergencyContactRelationship(request.getEmergencyContactRelationship())
            .profilePicture("/default-profile.png")
            .biography(request.getBiography())
            .school(school)
            .isApproved(false)
            .build();

        teacher = teacherRepository.save(teacher);

        // Update school teacher count
        school.setTeacherCount(school.getTeacherCount() + 1);
        schoolRepository.save(school);

        // Send notification email
        emailService.sendTeacherRegistrationNotification(teacher);

        return TeacherResponse.fromTeacher(teacher);
    }

    public TeacherResponse getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
        return TeacherResponse.fromTeacher(teacher);
    }

    public Page<TeacherResponse> searchTeachers(
        Long schoolId,
        String specialization,
        Boolean isApproved,
        Boolean isActive,
        Pageable pageable
    ) {
        Page<Teacher> teachers = teacherRepository.searchTeachers(
            schoolId, specialization, isApproved, isActive, pageable);
        return teachers.map(TeacherResponse::fromTeacher);
    }

    public TeacherResponse updateTeacher(Long id, TeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        School school = schoolRepository.findById(request.getSchoolId())
            .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + request.getSchoolId()));

        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setPhone(request.getPhone());
        teacher.setDateOfBirth(request.getDateOfBirth());
        teacher.setGender(request.getGender());
        teacher.setAddress(request.getAddress());
        teacher.setCity(request.getCity());
        teacher.setPostalCode(request.getPostalCode());
        teacher.setCountry(request.getCountry());
        teacher.setNationalId(request.getNationalId());
        teacher.setQualification(request.getQualification());
        teacher.setSpecialization(request.getSpecialization());
        teacher.setYearsOfExperience(request.getYearsOfExperience());
        teacher.setEmergencyContactName(request.getEmergencyContactName());
        teacher.setEmergencyContactPhone(request.getEmergencyContactPhone());
        teacher.setEmergencyContactRelationship(request.getEmergencyContactRelationship());
        teacher.setBiography(request.getBiography());
        teacher.setSchool(school);

        teacher = teacherRepository.save(teacher);
        return TeacherResponse.fromTeacher(teacher);
    }

    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        // Update school teacher count
        if (teacher.getSchool() != null) {
            School school = teacher.getSchool();
            school.setTeacherCount(school.getTeacherCount() - 1);
            schoolRepository.save(school);
        }

        teacherRepository.delete(teacher);
    }

    public TeacherResponse approveTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        teacher.setIsApproved(true);
        teacher.setRejectionReason(null);
        teacher = teacherRepository.save(teacher);

        // Send approval email
        emailService.sendTeacherApprovalNotification(teacher);

        return TeacherResponse.fromTeacher(teacher);
    }

    public TeacherResponse rejectTeacher(Long id, String reason) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        teacher.setIsApproved(false);
        teacher.setRejectionReason(reason);
        teacher = teacherRepository.save(teacher);

        // Send rejection email
        emailService.sendTeacherRejectionNotification(teacher, reason);

        return TeacherResponse.fromTeacher(teacher);
    }

    public TeacherResponse uploadProfilePicture(Long id, MultipartFile file) throws IOException {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        String uploadDir = "/uploads/teachers/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        teacher.setProfilePicture("/uploads/teachers/" + fileName);
        teacher = teacherRepository.save(teacher);

        return TeacherResponse.fromTeacher(teacher);
    }

    public Object getTeacherQualifications(Long id) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
        return teacher.getQualifications();
    }

    public Object getTeacherExperiences(Long id) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
        return teacher.getExperiences();
    }

    public Object getTeacherDocuments(Long id) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
        return teacher.getDocuments();
    }

    public Object getTeacherStats() {
        long totalTeachers = teacherRepository.count();
        long approvedTeachers = teacherRepository.countByIsApproved(true);
        long pendingTeachers = teacherRepository.countByIsApproved(false);
        long activeTeachers = teacherRepository.countByIsActive(true);

        return new Object() {
            public long getTotalTeachers() { return totalTeachers; }
            public long getApprovedTeachers() { return approvedTeachers; }
            public long getPendingTeachers() { return pendingTeachers; }
            public long getActiveTeachers() { return activeTeachers; }
        };
    }
}
