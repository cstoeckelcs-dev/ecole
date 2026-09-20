package com.ecole.service;

import com.ecole.dto.SchoolRequest;
import com.ecole.dto.SchoolResponse;
import com.ecole.exception.ResourceNotFoundException;
import com.ecole.model.School;
import com.ecole.repository.SchoolRepository;
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
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final EmailService emailService;

    public SchoolResponse createSchool(SchoolRequest request) {
        if (schoolRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("School with email already exists");
        }
        if (schoolRepository.existsByName(request.getName())) {
            throw new RuntimeException("School with name already exists");
        }
        if (schoolRepository.existsByAccreditationNumber(request.getAccreditationNumber())) {
            throw new RuntimeException("School with accreditation number already exists");
        }
        if (schoolRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new RuntimeException("School with license number already exists");
        }

        School school = School.builder()
            .name(request.getName())
            .address(request.getAddress())
            .city(request.getCity())
            .postalCode(request.getPostalCode())
            .country(request.getCountry())
            .email(request.getEmail())
            .phone(request.getPhone())
            .website(request.getWebsite())
            .foundedDate(request.getFoundedDate())
            .principalName(request.getPrincipalName())
            .principalEmail(request.getPrincipalEmail())
            .principalPhone(request.getPrincipalPhone())
            .accreditationNumber(request.getAccreditationNumber())
            .licenseNumber(request.getLicenseNumber())
            .description(request.getDescription())
            .type(request.getType())
            .level(request.getLevel())
            .logoUrl("/default-logo.png")
            .isApproved(false)
            .build();

        school = schoolRepository.save(school);

        // Send notification email
        emailService.sendSchoolRegistrationNotification(school);

        return SchoolResponse.fromSchool(school);
    }

    public SchoolResponse getSchoolById(Long id) {
        School school = schoolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));
        return SchoolResponse.fromSchool(school);
    }

    public Page<SchoolResponse> searchSchools(
        String name,
        String city,
        String country,
        String type,
        String level,
        Boolean isApproved,
        Boolean isActive,
        Pageable pageable
    ) {
        School.SchoolType schoolType = type != null ? School.SchoolType.valueOf(type) : null;
        School.SchoolLevel schoolLevel = level != null ? School.SchoolLevel.valueOf(level) : null;

        Page<School> schools = schoolRepository.searchSchools(
            name, city, country, schoolType, schoolLevel, isApproved, isActive, pageable);

        return schools.map(SchoolResponse::fromSchool);
    }

    public SchoolResponse updateSchool(Long id, SchoolRequest request) {
        School school = schoolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));

        school.setName(request.getName());
        school.setAddress(request.getAddress());
        school.setCity(request.getCity());
        school.setPostalCode(request.getPostalCode());
        school.setCountry(request.getCountry());
        school.setEmail(request.getEmail());
        school.setPhone(request.getPhone());
        school.setWebsite(request.getWebsite());
        school.setFoundedDate(request.getFoundedDate());
        school.setPrincipalName(request.getPrincipalName());
        school.setPrincipalEmail(request.getPrincipalEmail());
        school.setPrincipalPhone(request.getPrincipalPhone());
        school.setAccreditationNumber(request.getAccreditationNumber());
        school.setLicenseNumber(request.getLicenseNumber());
        school.setDescription(request.getDescription());
        school.setType(request.getType());
        school.setLevel(request.getLevel());

        school = schoolRepository.save(school);
        return SchoolResponse.fromSchool(school);
    }

    public void deleteSchool(Long id) {
        School school = schoolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));
        schoolRepository.delete(school);
    }

    public SchoolResponse approveSchool(Long id) {
        School school = schoolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));

        school.setIsApproved(true);
        school.setRejectionReason(null);
        school = schoolRepository.save(school);

        // Send approval email
        emailService.sendSchoolApprovalNotification(school);

        return SchoolResponse.fromSchool(school);
    }

    public SchoolResponse rejectSchool(Long id, String reason) {
        School school = schoolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));

        school.setIsApproved(false);
        school.setRejectionReason(reason);
        school = schoolRepository.save(school);

        // Send rejection email
        emailService.sendSchoolRejectionNotification(school, reason);

        return SchoolResponse.fromSchool(school);
    }

    public SchoolResponse uploadLogo(Long id, MultipartFile file) throws IOException {
        School school = schoolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));

        String uploadDir = "/uploads/schools/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        school.setLogoUrl("/uploads/schools/" + fileName);
        school = schoolRepository.save(school);

        return SchoolResponse.fromSchool(school);
    }

    public Page<?> getSchoolTeachers(Long id, Pageable pageable) {
        School school = schoolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));
        return null; // TODO: Implement
    }

    public Object getSchoolStats() {
        long totalSchools = schoolRepository.count();
        long approvedSchools = schoolRepository.countByIsApproved(true);
        long pendingSchools = schoolRepository.countByIsApproved(false);
        long activeSchools = schoolRepository.countByIsActive(true);

        return new Object() {
            public long getTotalSchools() { return totalSchools; }
            public long getApprovedSchools() { return approvedSchools; }
            public long getPendingSchools() { return pendingSchools; }
            public long getActiveSchools() { return activeSchools; }
        };
    }
}
