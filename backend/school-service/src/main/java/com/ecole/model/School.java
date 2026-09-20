package com.ecole.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "schools")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String website;

    @Column(nullable = false)
    private LocalDate foundedDate;

    @Column(nullable = false)
    private String principalName;

    @Column(nullable = false)
    private String principalEmail;

    @Column(nullable = false)
    private String principalPhone;

    @Column(nullable = false)
    private String accreditationNumber;

    @Column(nullable = false)
    private String licenseNumber;

    @Column(nullable = false)
    private String logoUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isApproved = false;

    @Column
    private String rejectionReason;

    @Column(nullable = false)
    @Builder.Default
    private Integer studentCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer teacherCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer classroomCount = 0;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<SchoolDocument> documents = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SchoolType type = SchoolType.PRIVATE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SchoolLevel level = SchoolLevel.PRIMARY;

    public enum SchoolType {
        PUBLIC, PRIVATE, INTERNATIONAL, RELIGIOUS, SPECIAL
    }

    public enum SchoolLevel {
        PRESCHOOL, PRIMARY, SECONDARY, HIGH_SCHOOL, VOCATIONAL, UNIVERSITY
    }
}
