package com.example.memoservice.domain.baby.model;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "baby_measurement", indexes = {
        @Index(name = "baby_measurement_idx", columnList = "baby_id"),
})
public class BabyMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long babyMeasurementId;

    private Long babyId;

    private double weight;

    private double height;

    private ZonedDateTime measurementTime;


    public BabyMeasurement(Long babyId, double weight, double height) {
        this.babyId = babyId;
        this.weight = weight;
        this.height = height;
        this.measurementTime = ZonedDateTime.now();
    }

    public void updateData(double weight, double height) {
        this.weight = weight;
        this.height = height;
        this.measurementTime = ZonedDateTime.now();
    }


}
