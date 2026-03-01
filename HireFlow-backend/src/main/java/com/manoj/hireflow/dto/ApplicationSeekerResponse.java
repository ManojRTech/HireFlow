package com.manoj.hireflow.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApplicationSeekerResponse {

    private Long applicationId;
    private String jobTitle;
    private String employerName;
    private String status;
    private LocalDateTime appliedAt;
    private String statusMessage;
}