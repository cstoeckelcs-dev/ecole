package com.ecole.controller;

import com.ecole.dto.TeacherRequest;
import com.ecole.dto.TeacherResponse;
import com.ecole.service.TeacherService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<TeacherResponse> createTeacher(@RequestBody TeacherRequest request) {
        TeacherResponse response = teacherService.createTeacher(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponse> getTeacherById(@PathVariable Long id) {
        TeacherResponse response = teacherService.getTeacherById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<TeacherResponse>> getAllTeachers(
        @RequestParam(required = false) Long schoolId,
        @RequestParam(required = false) String specialization,
        @RequestParam(required = false) Boolean isApproved,
        @RequestParam(required = false) Boolean isActive,
        Pageable pageable
    ) {
        Page<TeacherResponse> response = teacherService.searchTeachers(
            schoolId, specialization, isApproved, isActive, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponse> updateTeacher(
        @PathVariable Long id,
        @RequestBody TeacherRequest request
    ) {
        TeacherResponse response = teacherService.updateTeacher(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<TeacherResponse> approveTeacher(@PathVariable Long id) {
        TeacherResponse response = teacherService.approveTeacher(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<TeacherResponse> rejectTeacher(
        @PathVariable Long id,
        @RequestParam String reason
    ) {
        TeacherResponse response = teacherService.rejectTeacher(id, reason);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/profile-picture")
    public ResponseEntity<TeacherResponse> uploadProfilePicture(
        @PathVariable Long id,
        @RequestParam MultipartFile file
    ) throws IOException {
        TeacherResponse response = teacherService.uploadProfilePicture(id, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/qualifications")
    public ResponseEntity<?> getTeacherQualifications(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherQualifications(id));
    }

    @GetMapping("/{id}/experiences")
    public ResponseEntity<?> getTeacherExperiences(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherExperiences(id));
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<?> getTeacherDocuments(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherDocuments(id));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getTeacherStats() {
        return ResponseEntity.ok(teacherService.getTeacherStats());
    }
}
