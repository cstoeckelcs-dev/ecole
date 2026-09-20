package com.ecole.controller;

import com.ecole.dto.SchoolRequest;
import com.ecole.dto.SchoolResponse;
import com.ecole.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @PostMapping
    public ResponseEntity<SchoolResponse> createSchool(@RequestBody SchoolRequest request) {
        SchoolResponse response = schoolService.createSchool(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolResponse> getSchoolById(@PathVariable Long id) {
        SchoolResponse response = schoolService.getSchoolById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<SchoolResponse>> getAllSchools(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String country,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String level,
        @RequestParam(required = false) Boolean isApproved,
        @RequestParam(required = false) Boolean isActive,
        Pageable pageable
    ) {
        Page<SchoolResponse> response = schoolService.searchSchools(
            name, city, country, type, level, isApproved, isActive, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolResponse> updateSchool(
        @PathVariable Long id,
        @RequestBody SchoolRequest request
    ) {
        SchoolResponse response = schoolService.updateSchool(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        schoolService.deleteSchool(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<SchoolResponse> approveSchool(@PathVariable Long id) {
        SchoolResponse response = schoolService.approveSchool(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<SchoolResponse> rejectSchool(
        @PathVariable Long id,
        @RequestParam String reason
    ) {
        SchoolResponse response = schoolService.rejectSchool(id, reason);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/logo")
    public ResponseEntity<SchoolResponse> uploadLogo(
        @PathVariable Long id,
        @RequestParam MultipartFile file
    ) {
        SchoolResponse response = schoolService.uploadLogo(id, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/teachers")
    public ResponseEntity<?> getSchoolTeachers(
        @PathVariable Long id,
        Pageable pageable
    ) {
        return ResponseEntity.ok(schoolService.getSchoolTeachers(id, pageable));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getSchoolStats() {
        return ResponseEntity.ok(schoolService.getSchoolStats());
    }
}
