package com.example.memoservice.domain.baby.repository;

import com.example.memoservice.domain.baby.model.BabyMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BabyMeasurementRepository extends JpaRepository<BabyMeasurement, Long> {

    Optional<BabyMeasurement> findByBabyId(Long babyId);

}
