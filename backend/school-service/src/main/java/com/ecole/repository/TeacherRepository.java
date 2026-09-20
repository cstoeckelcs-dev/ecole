package com.ecole.repository;

import com.ecole.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findByNationalId(String nationalId);
    Boolean existsByEmail(String email);
    Boolean existsByNationalId(String nationalId);

    List<Teacher> findBySchoolId(Long schoolId);
    List<Teacher> findByIsApproved(Boolean isApproved);
    List<Teacher> findByIsActive(Boolean isActive);
    List<Teacher> findBySpecializationContainingIgnoreCase(String specialization);
    List<Teacher> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    @Query("SELECT t FROM Teacher t WHERE t.school.id = :schoolId")
    Page<Teacher> findBySchoolId(Long schoolId, Pageable pageable);

    @Query("SELECT t FROM Teacher t WHERE " +
           "(:schoolId IS NULL OR t.school.id = :schoolId) AND " +
           "(:specialization IS NULL OR t.specialization LIKE %:specialization%) AND " +
           "(:isApproved IS NULL OR t.isApproved = :isApproved) AND " +
           "(:isActive IS NULL OR t.isActive = :isActive)")
    Page<Teacher> searchTeachers(
        Long schoolId,
        String specialization,
        Boolean isApproved,
        Boolean isActive,
        Pageable pageable
    );
}
