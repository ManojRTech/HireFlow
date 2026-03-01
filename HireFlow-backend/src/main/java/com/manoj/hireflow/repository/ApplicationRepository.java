package com.manoj.hireflow.repository;

import com.manoj.hireflow.entity.Application;
import com.manoj.hireflow.entity.Job;
import com.manoj.hireflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByUserAndJob(User user, Job job);
    List<Application> findByJob(Job job);
    List<Application> findByUserId(Long userId);
    boolean existsByJob(Job job);
}
