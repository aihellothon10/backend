package com.example.memoservice.domain.baby;

import com.example.memoservice.domain.baby.model.BabyMeasurement;
import com.example.memoservice.domain.baby.repository.BabyMeasurementRepository;
import com.example.memoservice.domain.baby.repository.BabyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BabyQueryService {

    private final BabyRepository babyRepository;
    private final BabyMeasurementRepository babyMeasurementRepository;

    public List<BabyMeasurementDto> getBabyMeasurements(Long babyId) {
        List<BabyMeasurement> measurements = babyMeasurementRepository.findAll();
        return measurements.stream()
                .map(m -> new BabyMeasurementDto(m.getWeight(), m.getHeight()))
                .toList();
    }
    
}
