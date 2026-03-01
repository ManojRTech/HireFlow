package com.manoj.hireflow.controller;

import com.manoj.hireflow.dto.JobCreateRequest;
import com.manoj.hireflow.dto.JobResponse;
import com.manoj.hireflow.service.JobService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<Page<JobResponse>> getAllJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "latest") String sort,
            @RequestParam(required = false) String company,
            Pageable pageable
    ) {

        Sort sorting;

        switch (sort) {
            case "salary_asc":
                sorting = Sort.by("salary").ascending();
                break;
            case "salary_desc":
                sorting = Sort.by("salary").descending();
                break;
            default:
                sorting = Sort.by("createdAt").descending(); // latest
        }

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sorting
        );

        return ResponseEntity.ok(
                jobService.filterJobs(keyword, company, sortedPageable)
        );
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJobById(
            @PathVariable Long jobId
    ) {
        return ResponseEntity.ok(
                jobService.getJobById(jobId)
        );
    }

    // Employer creates job
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody JobCreateRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(jobService.createJob(request, email));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<JobResponse>> getMyJobs(Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                jobService.getMyJobs(email)
        );
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<String> deleteJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                jobService.deleteJob(jobId, email)
        );
    }

    @PutMapping("/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long jobId,
            @Valid @RequestBody JobCreateRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(
                jobService.updateJob(jobId, request, email)
        );
    }
}