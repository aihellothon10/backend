package com.example.memoservice.domain.analizer;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.analizer.event.JobCreatedEvent;
import com.example.memoservice.domain.analizer.model.*;
import com.example.memoservice.domain.analizer.repository.JobRepository;
import com.example.memoservice.domain.analizer.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JobCommandService {

    private final JobRepository jobRepository;
    private final TaskRepository taskRepository;

    private final ApplicationEventPublisher eventPublisher;


    /**
     * 메모 분석용
     */
    public Long createMemoJob(String name) {
        Job job = new Job(name, JobType.Memo);
//        job.addTask(new Task(TaskType.DiffBot, "Diffbot input"));
        job.addTask(new Task(TaskType.HelpyV, 2));
        job.addTask(new Task(TaskType.NaverSearch, 4));

        Job savedJob = jobRepository.save(job);

        // API 호출 요청은 이벤트로 넘겨서 Task로 처리한다.
        eventPublisher.publishEvent(new JobCreatedEvent(savedJob.getJobId()));
        job.updateStatus(AnalysisStatus.InProgress);
        // 클라이언트에서 작업 정보 조회를 위해 jobId 반환
        return savedJob.getJobId();
    }

    /**
     * 질문 답변용
     */
    public Long createQuestionJob(String name) {
        Job job = new Job(name, JobType.Question);
//        job.addTask(new Task(TaskType.DiffBot, "Diffbot input"));
        job.addTask(new Task(TaskType.Perplexity, 3));
//        job.addTask(new Task(TaskType.NaverSearch, 4));

        Job savedJob = jobRepository.save(job);

        eventPublisher.publishEvent(new JobCreatedEvent(savedJob.getJobId()));
        job.updateStatus(AnalysisStatus.InProgress);

        return savedJob.getJobId();
    }


    public void completeJob(Long jobId) {
        Job job = findJob(jobId);
        job.updateStatus(AnalysisStatus.Completed);
    }

    private Job findJob(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new DataNotFoundException("Job Not Found"));
    }

}
