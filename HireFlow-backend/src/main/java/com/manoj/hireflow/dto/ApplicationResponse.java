package com.manoj.hireflow.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApplicationResponse {

    private Long applicationId;
    private String applicantName;
    private String applicantEmail;
    private String status;
    private String statusMessage;
    private String resumeUrl;
    private LocalDateTime appliedAt;
}
