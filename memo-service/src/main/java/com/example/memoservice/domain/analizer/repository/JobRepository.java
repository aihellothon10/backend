package com.example.memoservice.domain.analizer.repository;

import com.example.memoservice.domain.analizer.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
