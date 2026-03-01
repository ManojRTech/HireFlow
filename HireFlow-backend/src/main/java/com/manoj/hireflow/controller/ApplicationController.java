package com.manoj.hireflow.controller;

import com.manoj.hireflow.dto.ApplicationResponse;
import com.manoj.hireflow.dto.ApplicationSeekerResponse;
import com.manoj.hireflow.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // JobSeeker applies to job
    @PostMapping("/{jobId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<String> applyToJob(
            @PathVariable Long jobId,
            @RequestParam("resume") MultipartFile file,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(
                applicationService.applyToJob(jobId, email, file)
        );
    }

    // Employer views applications for a job
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<ApplicationResponse>> viewApplications(
            @PathVariable Long jobId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(
                applicationService.getApplicationsForJob(jobId, email)
        );
    }

    // Employer updates status
    @PutMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long applicationId,
            @RequestParam String status,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(
                applicationService.updateApplicationStatus(applicationId, status, email)
        );
    }

    // JobSeeker views own applications
    @GetMapping("/me")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<List<ApplicationSeekerResponse>> getMyApplications(
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(
                applicationService.getMyApplications(email)
        );
    }

    @DeleteMapping("/{applicationId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<String> cancelApplication(
            @PathVariable Long applicationId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                applicationService.cancelApplication(applicationId, email)
        );
    }
}