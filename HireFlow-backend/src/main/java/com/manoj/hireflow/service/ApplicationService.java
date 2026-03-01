package com.manoj.hireflow.service;

import com.manoj.hireflow.dto.ApplicationResponse;
import com.manoj.hireflow.dto.ApplicationSeekerResponse;
import com.manoj.hireflow.entity.Application;
import com.manoj.hireflow.entity.Job;
import com.manoj.hireflow.entity.User;
import com.manoj.hireflow.repository.ApplicationRepository;
import com.manoj.hireflow.repository.JobRepository;
import com.manoj.hireflow.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              JobRepository jobRepository,
                              UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public String applyToJob(Long jobId, String email, MultipartFile file) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().name().equals("JOB_SEEKER")) {
            throw new RuntimeException("Only job seekers can apply");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (applicationRepository.existsByUserAndJob(user, job)) {
            throw new RuntimeException("You already applied to this job");
        }

        String uploadDir = "uploads/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        try {
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload resume");
        }

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setResumeUrl(fileName);
        application.setStatus(Application.ApplicationStatus.PENDING);
        application.setStatusMessage("Your application is under review.");

        applicationRepository.save(application);

        return "Application submitted successfully";
    }

    public List<ApplicationResponse> getApplicationsForJob(Long jobId, String email) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Ownership validation
        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new RuntimeException("You are not allowed to view applications for this job");
        }

        List<Application> applications = applicationRepository.findByJob(job);

        return applications.stream()
                .map(this::convertToResponse)
                .toList();
    }
    private ApplicationResponse convertToResponse(Application application) {

        ApplicationResponse response = new ApplicationResponse();

        response.setApplicationId(application.getId());
        response.setApplicantName(application.getUser().getName());
        response.setApplicantEmail(application.getUser().getEmail());
        response.setStatus(application.getStatus().name());
        response.setAppliedAt(application.getAppliedAt());
        response.setResumeUrl(application.getResumeUrl());
        response.setStatusMessage(application.getStatusMessage());

        return response;
    }

    public String updateApplicationStatus(Long applicationId,
                                          String newStatus,
                                          String email) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Job job = application.getJob();

        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new RuntimeException("Not authorized");
        }

        application.setStatus(Application.ApplicationStatus.valueOf(newStatus));

        switch (Application.ApplicationStatus.valueOf(newStatus)) {
            case SHORTLISTED ->
                    application.setStatusMessage(
                            "Congratulations! Your profile has been shortlisted for the next stage. Further communication regarding the upcoming process will be shared with you via email.");
            case REJECTED ->
                    application.setStatusMessage(
                            "Thank you for your interest in this opportunity. After careful consideration, we will not be moving forward with your application at this time.");
            case PENDING ->
                    application.setStatusMessage(
                            "Your application is under review.");
        }

        applicationRepository.save(application);

        return "Application status updated successfully";
    }

    public List<ApplicationSeekerResponse> getMyApplications(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Application> applications =
                applicationRepository.findByUserId(user.getId());

        return applications.stream()
                .map(this::convertToSeekerResponse)
                .toList();
    }
    private ApplicationSeekerResponse convertToSeekerResponse(Application application) {

        ApplicationSeekerResponse response = new ApplicationSeekerResponse();

        response.setApplicationId(application.getId());
        response.setJobTitle(application.getJob().getTitle());
        response.setEmployerName(application.getJob().getEmployer().getName());
        response.setStatus(application.getStatus().name());
        response.setAppliedAt(application.getAppliedAt());
        response.setStatusMessage(application.getStatusMessage());

        return response;
    }

    public String cancelApplication(Long applicationId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized");
        }

        applicationRepository.delete(application);

        return "Application cancelled successfully";
    }
}
