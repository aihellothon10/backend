package com.example.memoservice.domain.analizer;

import com.example.memoservice.domain.analizer.event.TaskStartEvent;
import com.example.memoservice.domain.analizer.model.JobType;
import com.example.memoservice.domain.analizer.model.TaskType;
import com.example.memoservice.domain.analizer.service.DataMapper;
import com.example.memoservice.domain.analizer.service.helpyv.HelpyVService;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatRequest;
import com.example.memoservice.domain.media.application.MediaCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AnalyzeCommandService {

    private final JobCommandService jobCommandService;
    private final TaskCommandService taskCommandService;
    private final TaskQueryService taskQueryService;
    private final ApplicationEventPublisher eventPublisher;

    private final MediaCommandService mediaCommandService;


    /**
     * Job을 생성하고 분석을 위해 API 호출
     * - 분석 순서에 맞게 호출해야한다.
     */
    public Long requestAnalyze(AnalyzeRequest request, List<MultipartFile> images) {
        List<String> imageIds = new ArrayList<>();
        if (images != null) {
            images.forEach(image -> {
                        String uuid = mediaCommandService.saveFile(image);
                        imageIds.add(uuid);
                    }
            );
        }

        Long jobId;
        if (request.getJobType() == JobType.Question) {
            jobId = jobCommandService.createQuestionJob(request, imageIds);
        } else if (request.getJobType() == JobType.Memo) {
            jobId = jobCommandService.createMemoJob(request, imageIds);
        } else {
            throw new RuntimeException("Invalid JobType");
        }

        // diffBotTask가 있다면
        var diffBotTask = taskQueryService.getTaskByTaskType(jobId, TaskType.DiffBot);
        diffBotTask.ifPresentOrElse(taskRequestDto -> {
//                    taskCommandService.setInput(taskRequestDto.getTaskId(), Map.of("query", request.getQuery()));
                    taskCommandService.setInput(taskRequestDto.getTaskId(),
                            DataMapper.toMapWithMapper(HelpyVService.createRequest(List.of(request.getQuery())))
                    );

                    eventPublisher.publishEvent(new TaskStartEvent(jobId, taskRequestDto.getTaskId()));
                },
                () -> {
                    taskQueryService.getTaskByTaskType(jobId, TaskType.HelpyV)
                            .ifPresent(helpyVTask -> {
                                        taskCommandService.setInput(
                                                helpyVTask.getTaskId(),
                                                DataMapper.toMapWithMapper(HelpyVService.createRequest(List.of(request.getQuery())))
                                        );
                                        eventPublisher.publishEvent(new TaskStartEvent(jobId, helpyVTask.getTaskId()));
                                    }
                            );
                });

        return jobId;
    }

    /**
     * 채팅에서 분석요청하는 것
     * <p>
     * - HelpyV의 메시지 프롬프트 전체를 입력해야한다.
     */
    public Long requestAnalyzeForChat(AnalyzeRequest request, HelpyVChatRequest helpyRequest, List<MultipartFile> images) {
        List<String> imageIds = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            images.forEach(image -> {
                        String uuid = mediaCommandService.saveFile(image);
                        imageIds.add(uuid);
                    }
            );
        }

        Long jobId;
        if (request.getJobType() == JobType.Question) {
            jobId = jobCommandService.createQuestionJob(request, imageIds);
        } else if (request.getJobType() == JobType.Memo) {
            jobId = jobCommandService.createMemoJob(request, imageIds);
        } else {
            throw new RuntimeException("Invalid JobType");
        }

        // diffBotTask가 있다면
        var diffBotTask = taskQueryService.getTaskByTaskType(jobId, TaskType.DiffBot);
        diffBotTask.ifPresentOrElse(taskRequestDto -> {
                    // 중요 : Diffbot에서 helpyV의 request를 전닳한다...
                    taskCommandService.setInput(taskRequestDto.getTaskId(),
                            DataMapper.toMapWithMapper(helpyRequest)
                    );
                    eventPublisher.publishEvent(new TaskStartEvent(jobId, taskRequestDto.getTaskId()));
                },
                () -> {
                    taskQueryService.getTaskByTaskType(jobId, TaskType.HelpyV)
                            .ifPresent(helpyVTask -> {
                                        taskCommandService.setInput(
                                                helpyVTask.getTaskId(),
                                                DataMapper.toMapWithMapper(helpyRequest)
                                        );
                                        eventPublisher.publishEvent(new TaskStartEvent(jobId, helpyVTask.getTaskId()));
                                    }
                            );
                });

        return jobId;
    }


}
