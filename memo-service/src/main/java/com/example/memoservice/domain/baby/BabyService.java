package com.example.memoservice.domain.baby;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.baby.model.Baby;
import com.example.memoservice.domain.baby.model.BabyMeasurement;
import com.example.memoservice.domain.baby.repository.BabyMeasurementRepository;
import com.example.memoservice.domain.baby.repository.BabyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BabyService {

    private final BabyRepository babyRepository;
    private final BabyMeasurementRepository babyMeasurementRepository;


    public Long createBaby(String name) {
        return babyRepository.save(new Baby(name)).getBabyId();
    }

    public void addMeasurement(Long babyId, double weight, double height) {
        findBaby(babyId);

        BabyMeasurement babyMeasurement = new BabyMeasurement(babyId, weight, height);
        babyMeasurementRepository.save(babyMeasurement);

    }

    private Baby findBaby(Long babyId) {
        return babyRepository.findById(babyId)
                .orElseThrow(() -> new DataNotFoundException("Baby Not Found"));
    }


}
