package com.manoj.hireflow.service;

import com.manoj.hireflow.dto.JobCreateRequest;
import com.manoj.hireflow.dto.JobResponse;
import com.manoj.hireflow.entity.Application;
import com.manoj.hireflow.entity.Job;
import com.manoj.hireflow.entity.User;
import com.manoj.hireflow.repository.ApplicationRepository;
import com.manoj.hireflow.repository.JobRepository;
import com.manoj.hireflow.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository, ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    public JobResponse createJob(JobCreateRequest request, String email) {
         User employer = userRepository.findByEmail(email)
                 .orElseThrow(() -> new RuntimeException("User not found"));

         if (!employer.getRole().name().equals("EMPLOYER")) {
             throw new RuntimeException("Only employers can post jobs");
         }

         Job job = new Job();
         job.setTitle(request.getTitle());
         job.setDescription(request.getDescription());
         job.setCompany(request.getCompany());
         job.setLocation(request.getLocation());
         job.setSalary(request.getSalary());
         job.setEmployer(employer);

        Job savedJob = jobRepository.save(job);

        return convertToResponse(savedJob);
    }

    private JobResponse convertToResponse(Job job) {

        JobResponse response = new JobResponse();

        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setCompany(job.getCompany());
        response.setLocation(job.getLocation());
        response.setSalary(job.getSalary());
        response.setEmployerName(job.getEmployer().getName());
        response.setEmployerEmail(job.getEmployer().getEmail());
        response.setCreatedAt(job.getCreatedAt());

        return response;
    }

    public Page<JobResponse> getAllJobs(Pageable pageable) {

        Page<Job> jobPage = jobRepository.findAll(pageable);

        return jobPage.map(this::convertToResponse);
    }

    public List<JobResponse> getMyJobs(String email) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Job> jobs = jobRepository.findByEmployer(employer);

        return jobs.stream()
                .map(this::convertToResponse)
                .toList();
    }

    public String deleteJob(Long jobId, String email) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Check ownership
        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new RuntimeException("You are not authorized to delete this job");
        }

        // Check if applications exist
        if (applicationRepository.existsByJob(job)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot delete job. Applications already exist."
            );
        }

        jobRepository.delete(job);

        return "Job deleted successfully";
    }

    public JobResponse updateJob(Long jobId,
                                 JobCreateRequest request,
                                 String email) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new RuntimeException("Not authorized");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());

        jobRepository.save(job);

        return convertToResponse(job);
    }

    public Page<JobResponse> searchJobs(String keyword, Pageable pageable) {

        Page<Job> jobs = jobRepository
                .findByTitleContainingIgnoreCaseOrCompanyContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword,
                        pageable
                );

        return jobs.map(this::convertToResponse);
    }

    public Page<JobResponse> filterJobs(String keyword,
                                        String company,
                                        Pageable pageable) {

        if (keyword != null && !keyword.trim().isEmpty()) {
            return searchJobs(keyword, pageable);
        }

        if (company != null && !company.trim().isEmpty()) {
            return jobRepository
                    .findByCompanyContainingIgnoreCase(company, pageable)
                    .map(this::convertToResponse);
        }

        return getAllJobs(pageable);
    }

    public JobResponse getJobById(Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return convertToResponse(job);
    }
}

