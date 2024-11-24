package com.example.memoservice.domain.analizer;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.analizer.event.JobCreatedEvent;
import com.example.memoservice.domain.analizer.model.*;
import com.example.memoservice.domain.analizer.repository.JobRepository;
import com.example.memoservice.domain.analizer.service.DataMapper;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobCommandService {

    private final JobRepository jobRepository;
    private final TaskQueryService taskQueryService;

    private final ApplicationEventPublisher eventPublisher;


    /**
     * 메모 분석용
     */
    public Long createMemoJob(AnalyzeRequest request, List<String> imageIds) {
        Job job = new Job(
                JobType.Memo,
                request.getQuery(),
                imageIds,
                request.getLinks()
        );
        // 링크가 있을 경우에는 DiffBot으로 링크 분석 수행
        if (!CollectionUtils.isEmpty(request.getLinks())) {
            job.addTask(new Task(TaskType.DiffBot, 1));
        }
        job.addTask(new Task(TaskType.HelpyV, 2));
        job.addTask(new Task(TaskType.ToxicityPrediction, 3));
        job.addTask(new Task(TaskType.Perplexity, 4));
        job.addTask(new Task(TaskType.NaverSearch, 5));

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
    public Long createQuestionJob(AnalyzeRequest request, List<String> imageIds) {
        Job job = new Job(
                JobType.Memo,
                request.getQuery(),
                imageIds,
                request.getLinks()
        );
//        job.addTask(new Task(TaskType.DiffBot, "Diffbot input"));
        job.addTask(new Task(TaskType.Perplexity, 3));
//        job.addTask(new Task(TaskType.NaverSearch, 4));

        Job savedJob = jobRepository.save(job);

        eventPublisher.publishEvent(new JobCreatedEvent(savedJob.getJobId()));
        job.updateStatus(AnalysisStatus.InProgress);

        return savedJob.getJobId();
    }

    /**
     * 분석 완료 후 결과값을 Job에 저장
     *
     * @param jobId
     */
    public void updateAnalyzeResult(Long jobId) {
        Job job = findJob(jobId);

        List<Task> tasks = taskQueryService.getTasks(jobId);

        // TODO 일단 helpyv의 결과값으로 설정하기

        Task helpyVTask = tasks.stream()
                .filter(t -> t.getTaskType() == TaskType.HelpyV)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("결과값을 설정할 수 없음"));

        var result = DataMapper.toObjectWithMapper(helpyVTask.getResult(), HelpyVChatResponse.Content.class);
//        job.setAnalyzeResult(
//                result.getQuestionTitle(),
//                result.getAnswer(),
//                result.getContentContainBoolean(),
//                result.getContentContainBooleanExplain()
//        );
        job.updateStatus(AnalysisStatus.Completed);
    }

    public void startJob(Long jobId) {
        Job job = findJob(jobId);
        job.updateStatus(AnalysisStatus.InProgress);
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
