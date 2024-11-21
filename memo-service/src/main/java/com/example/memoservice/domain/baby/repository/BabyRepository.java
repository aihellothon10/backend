package com.example.memoservice.domain.baby.repository;

import com.example.memoservice.domain.baby.model.Baby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BabyRepository extends JpaRepository<Baby, Long> {
}
