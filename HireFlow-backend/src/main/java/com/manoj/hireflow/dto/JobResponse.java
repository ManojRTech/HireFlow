package com.manoj.hireflow.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String company;
    private String location;
    private BigDecimal salary;
    private String employerName;
    private String employerEmail;
    private LocalDateTime createdAt;
}
