package com.ecole.dto;

import com.ecole.model.Teacher;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private String nationalId;
    private String qualification;
    private String specialization;
    private Integer yearsOfExperience;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelationship;
    private String profilePicture;
    private String biography;
    private Boolean isActive;
    private Boolean isApproved;
    private String rejectionReason;
    private Teacher.TeacherStatus status;
    private Long schoolId;
    private String schoolName;

    public static TeacherResponse fromTeacher(Teacher teacher) {
        return TeacherResponse.builder()
            .id(teacher.getId())
            .firstName(teacher.getFirstName())
            .lastName(teacher.getLastName())
            .email(teacher.getEmail())
            .phone(teacher.getPhone())
            .dateOfBirth(teacher.getDateOfBirth())
            .gender(teacher.getGender())
            .address(teacher.getAddress())
            .city(teacher.getCity())
            .postalCode(teacher.getPostalCode())
            .country(teacher.getCountry())
            .nationalId(teacher.getNationalId())
            .qualification(teacher.getQualification())
            .specialization(teacher.getSpecialization())
            .yearsOfExperience(teacher.getYearsOfExperience())
            .emergencyContactName(teacher.getEmergencyContactName())
            .emergencyContactPhone(teacher.getEmergencyContactPhone())
            .emergencyContactRelationship(teacher.getEmergencyContactRelationship())
            .profilePicture(teacher.getProfilePicture())
            .biography(teacher.getBiography())
            .isActive(teacher.getIsActive())
            .isApproved(teacher.getIsApproved())
            .rejectionReason(teacher.getRejectionReason())
            .status(teacher.getStatus())
            .schoolId(teacher.getSchool() != null ? teacher.getSchool().getId() : null)
            .schoolName(teacher.getSchool() != null ? teacher.getSchool().getName() : null)
            .build();
    }

    public static List<TeacherResponse> fromTeachers(List<Teacher> teachers) {
        return teachers.stream()
            .map(TeacherResponse::fromTeacher)
            .collect(Collectors.toList());
    }
}
