package com.ecole.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "teacher_qualifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherQualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String degree;

    @Column(nullable = false)
    private String institution;

    @Column(nullable = false)
    private String fieldOfStudy;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isCompleted = true;

    @Column
    private String grade;

    @Column
    private String honors;

    @Column(nullable = false)
    private String country;

    @Column
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
