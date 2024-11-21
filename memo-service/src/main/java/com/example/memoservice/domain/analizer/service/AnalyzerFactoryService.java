package com.example.memoservice.domain.analizer.service;

import com.example.memoservice.domain.analizer.TaskCommandService;
import com.example.memoservice.domain.analizer.TaskQueryService;
import com.example.memoservice.domain.analizer.model.TaskStatus;
import com.example.memoservice.domain.analizer.model.TaskType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AnalyzerFactoryService {

    private final Map<String, AnalyzerService> serviceMap;

    private final TaskQueryService taskQueryService;
    private final TaskCommandService taskCommandService;

    // return next task id
    public Long process(Long currentTaskId) {
        var currentTask = taskQueryService.getTaskByTaskId(currentTaskId);
        currentTask.updateStatus(TaskStatus.InProgress);

        var service = getConvertService(currentTask.getTaskType());
        Long nextTaskId = null;
        try {
            nextTaskId = service.analyze(currentTaskId);
            currentTask.updateStatus(TaskStatus.Completed);
        } catch (Exception e) {
            currentTask.updateStatus(TaskStatus.Failed);
            e.printStackTrace();
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }

        return nextTaskId;
    }


    public AnalyzerService getConvertService(TaskType taskType) {
        return switch (taskType) {
//            case DiffBot -> serviceMap.get("diffBotService");
            case HelpyV -> serviceMap.get("helpyVService");
            case Perplexity -> serviceMap.get("perPlexityService");
            case NaverSearch -> serviceMap.get("naverSearchService");
            default -> throw new IllegalArgumentException("not supported task type");
        };
    }


//    public <I extends InputData, O extends OutputData> O serve(ServiceType serviceType, I inputData) {
//        ConvertService<I, O> service = getConvertService(serviceType);
//        return service.convert(inputData);
//    }

}
