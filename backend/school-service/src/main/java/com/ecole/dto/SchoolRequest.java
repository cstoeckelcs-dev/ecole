package com.ecole.dto;

import com.ecole.model.School;
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
public class SchoolRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String country;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String website;

    @NotNull
    private LocalDate foundedDate;

    @NotBlank
    private String principalName;

    @Email
    @NotBlank
    private String principalEmail;

    @NotBlank
    private String principalPhone;

    @NotBlank
    private String accreditationNumber;

    @NotBlank
    private String licenseNumber;

    @NotBlank
    private String description;

    @NotNull
    @Builder.Default
    private School.SchoolType type = School.SchoolType.PRIVATE;

    @NotNull
    @Builder.Default
    private School.SchoolLevel level = School.SchoolLevel.PRIMARY;
}
