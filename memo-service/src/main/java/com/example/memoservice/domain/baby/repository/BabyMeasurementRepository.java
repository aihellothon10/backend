package com.example.memoservice.domain.baby.repository;

import com.example.memoservice.domain.baby.model.BabyMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BabyMeasurementRepository extends JpaRepository<BabyMeasurement, Long> {
}
