package com.manoj.hireflow.repository;

import com.manoj.hireflow.entity.Job;
import com.manoj.hireflow.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByEmployer(User employer);
    Page<Job> findByTitleContainingIgnoreCaseOrCompanyContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title,
            String company,
            String description,
            Pageable pageable
    );
    Page<Job> findByCompanyContainingIgnoreCase(String company, Pageable pageable);
}
