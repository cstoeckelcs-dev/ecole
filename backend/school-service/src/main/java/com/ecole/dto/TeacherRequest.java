package com.ecole.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotNull
    private LocalDate dateOfBirth;

    @NotBlank
    private String gender;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String country;

    @NotBlank
    private String nationalId;

    @NotBlank
    private String qualification;

    @NotBlank
    private String specialization;

    @NotNull
    private Integer yearsOfExperience;

    @NotBlank
    private String emergencyContactName;

    @NotBlank
    private String emergencyContactPhone;

    @NotBlank
    private String emergencyContactRelationship;

    @NotBlank
    private String biography;

    @NotNull
    private Long schoolId;
}
