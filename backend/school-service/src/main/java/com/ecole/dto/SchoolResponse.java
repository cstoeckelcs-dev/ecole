package com.ecole.dto;

import com.ecole.model.School;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private String email;
    private String phone;
    private String website;
    private LocalDate foundedDate;
    private String principalName;
    private String principalEmail;
    private String principalPhone;
    private String accreditationNumber;
    private String licenseNumber;
    private String logoUrl;
    private String description;
    private Boolean isActive;
    private Boolean isApproved;
    private String rejectionReason;
    private Integer studentCount;
    private Integer teacherCount;
    private Integer classroomCount;
    private School.SchoolType type;
    private School.SchoolLevel level;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public static SchoolResponse fromSchool(School school) {
        return SchoolResponse.builder()
            .id(school.getId())
            .name(school.getName())
            .address(school.getAddress())
            .city(school.getCity())
            .postalCode(school.getPostalCode())
            .country(school.getCountry())
            .email(school.getEmail())
            .phone(school.getPhone())
            .website(school.getWebsite())
            .foundedDate(school.getFoundedDate())
            .principalName(school.getPrincipalName())
            .principalEmail(school.getPrincipalEmail())
            .principalPhone(school.getPrincipalPhone())
            .accreditationNumber(school.getAccreditationNumber())
            .licenseNumber(school.getLicenseNumber())
            .logoUrl(school.getLogoUrl())
            .description(school.getDescription())
            .isActive(school.getIsActive())
            .isApproved(school.getIsApproved())
            .rejectionReason(school.getRejectionReason())
            .studentCount(school.getStudentCount())
            .teacherCount(school.getTeacherCount())
            .classroomCount(school.getClassroomCount())
            .type(school.getType())
            .level(school.getLevel())
            .build();
    }

    public static List<SchoolResponse> fromSchools(List<School> schools) {
        return schools.stream()
            .map(SchoolResponse::fromSchool)
            .collect(Collectors.toList());
    }
}
