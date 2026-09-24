package com.ecole.repository;

import com.ecole.model.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    Optional<School> findByEmail(String email);
    Optional<School> findByName(String name);
    Optional<School> findByAccreditationNumber(String accreditationNumber);
    Optional<School> findByLicenseNumber(String licenseNumber);
    Boolean existsByEmail(String email);
    Boolean existsByName(String name);
    Boolean existsByAccreditationNumber(String accreditationNumber);
    Boolean existsByLicenseNumber(String licenseNumber);

    List<School> findByIsApproved(Boolean isApproved);
    List<School> findByIsActive(Boolean isActive);
    long countByIsApproved(boolean isApproved);
    long countByIsActive(boolean isActive);
    List<School> findByType(School.SchoolType type);
    List<School> findByLevel(School.SchoolLevel level);
    List<School> findByCityContainingIgnoreCase(String city);
    List<School> findByCountryContainingIgnoreCase(String country);
    List<School> findByNameContainingIgnoreCase(String name);

    @Query("SELECT s FROM School s WHERE " +
           "(:name IS NULL OR s.name LIKE %:name%) AND " +
           "(:city IS NULL OR s.city LIKE %:city%) AND " +
           "(:country IS NULL OR s.country LIKE %:country%) AND " +
           "(:type IS NULL OR s.type = :type) AND " +
           "(:level IS NULL OR s.level = :level) AND " +
           "(:isApproved IS NULL OR s.isApproved = :isApproved) AND " +
           "(:isActive IS NULL OR s.isActive = :isActive)")
    Page<School> searchSchools(
        String name,
        String city,
        String country,
        School.SchoolType type,
        School.SchoolLevel level,
        Boolean isApproved,
        Boolean isActive,
        Pageable pageable
    );
}
