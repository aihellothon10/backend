package com.example.memoservice.domain.information.repository;

import com.example.memoservice.domain.information.model.Information;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationRepository extends JpaRepository<Information, Long> {
}
