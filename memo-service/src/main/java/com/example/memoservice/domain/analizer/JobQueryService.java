package com.example.memoservice.domain.analizer;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.analizer.model.Job;
import com.example.memoservice.domain.analizer.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobQueryService {

    private final JobRepository jobRepository;


    public Job getJob(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new DataNotFoundException("Job Not Found"));
    }

}
