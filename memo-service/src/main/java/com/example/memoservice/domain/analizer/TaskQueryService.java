package com.example.memoservice.domain.analizer;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.analizer.model.Job;
import com.example.memoservice.domain.analizer.model.Task;
import com.example.memoservice.domain.analizer.model.TaskStatus;
import com.example.memoservice.domain.analizer.model.TaskType;
import com.example.memoservice.domain.analizer.repository.JobRepository;
import com.example.memoservice.domain.analizer.repository.TaskRepository;
import com.example.memoservice.domain.analizer.service.DataMapper;
import com.example.memoservice.domain.apiclient.diffbot.dto.DiffbotResponse;
import com.example.memoservice.domain.apiclient.naversearch.dto.NaverSearchResult;
import com.example.memoservice.domain.apiclient.perplexity.dto.PerplexityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskQueryService {

    private final TaskRepository taskRepository;
    private final JobRepository jobRepository;

    // TODO 왜 에러가 발생하는지 모르겠다...
    public List<Task> getTasks(Long jobId) {
        var v = taskRepository.findAllByJobJobId(jobId);
        return v;
    }

    public Optional<Task> getTaskByTaskType(Long jobId, TaskType taskType) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new DataNotFoundException("Job not found"));
        return job.getTasks().stream()
                .filter(t -> t.getTaskType() == taskType)
                .findFirst();

//        var tt = getTasks(jobId);
//
//        return tt.stream()
//                .filter(t -> t.getTaskType() == taskType)
//                .findFirst();
    }

    public Task getTaskByTaskId(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new DataNotFoundException("Task Not Found"));
    }

    public Task getNullableTaskByTaskId(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public boolean isCompleted(Long jobId) {
        return getTasks(jobId).stream()
                .allMatch(t -> t.getStatus() == TaskStatus.Completed);
    }

    public Task getNextTask(Long jobId, Long taskId) {
        List<Task> tasks = getTasks(jobId);

        // 현재 task의  sortOrder 기준으로 다음 위치에 있는 task return
        // sortOrder는 0보다 큰 정수이며 작은 수부터 큰수 순서로 next 선택
        Task currentTask = tasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found for id: " + taskId));
        return tasks.stream()
                .filter(task -> task.getSortOrder() > currentTask.getSortOrder())
                .min(Comparator.comparingInt(Task::getSortOrder))
                .orElse(null);
    }

    /**
     * 분석 중 각 Task의 결과에서 필요한 부분만 추출
     */
    public Map<String, Object> getTaskByTaskIdWithTaskType(Long taskId, TaskType taskType) {
        Task task = getTaskByTaskId(taskId);
        if (task.getTaskType() != taskType) {
            throw new RuntimeException("Task type is not match");
        }
        if (task.getStatus() != TaskStatus.Completed) {
            throw new RuntimeException("Task is not completed");
        }

        /**
         * DiffBot : 이미지 목록
         * HelpyV
         *  - 제목, 핵심 요약, 잘못된 정보 여부, 키워드
         * HelpyV -> ToxicityPrediction
         *  - 메모 내용을
         * Perplexity -> Naver Search
         * - Naver Search가 끝나면 모든 분석이 완료됨
         */

        if (taskType == TaskType.DiffBot) {
            // 이미지 목록 반환
            System.out.println("==========");
            System.out.println(task.getResponse());
//            List<DiffbotResponse> responses = (List<DiffbotResponse>) task.getResponse().get("responses");
            var r = ((List<Map<String, Object>>) task.getResponse().get("responses")).stream()
                    .map(m -> DataMapper.toObjectWithMapper(m, DiffbotResponse.class))
                    .toList();
            List<String> images = r.stream()
                    .map(d -> d.getObjects().getFirst().getImages())
                    .flatMap(List::stream)
                    .map(DiffbotResponse.ImageInfo::getUrl)
                    .toList();
            return Map.of("images", images);
        }
        if (task.getTaskType() == TaskType.HelpyV) {
            // 분석 결과 반환
            return task.getResponse();
//            HelpyVChatResponse response = DataMapper.toObjectWithMapper(task.getResponse(), HelpyVChatResponse.class);
//            var content = response.getChoices().getFirst().getMessage().getContent();
//            return Map.of(
//                    "questionTitle", content.getQuestionTitle(),
//                    "coreContentSummary", content.getCoreContentSummary(),
//                    "contentContainBoolean", content.getContentContainBoolean(),
//                    "contentContainBooleanExplain", content.getContentContainBooleanExplain(),
//                    "answer", content.getAnswer(),
//                    "keyword", content.getKeyword()
//            );
        }
        if (task.getTaskType() == TaskType.ToxicityPrediction) {
            Map<String, Object> request = task.getResponse();
            Map<String, Object> response = task.getResponse();
            return Map.of(
                    "request", task.getRequest().get("text"),
                    "response", task.getResponse().get("response")
            );
        }
        if (task.getTaskType() == TaskType.Perplexity) {
            PerplexityResponse response = DataMapper.toObjectWithMapper(task.getResponse(), PerplexityResponse.class);
            return Map.of(
                    "citations", response.citations(),
                    "content", response.choices().getFirst().message().content()
            );
        }
        if (task.getTaskType() == TaskType.NaverSearch) {
            Map<String, Object> request = task.getRequest();
            Map<String, Object> response = task.getResponse();
            var list = ((List<Map<String, Object>>) response.get("responses")).stream()
                    .map(m -> DataMapper.toObjectWithMapper(m, NaverSearchResult.class))
                    .toList();
            var items = list.stream()
                    .map(l -> l.items())
                    .toList();

            return Map.of(
                    "keywords", request.get("keywords"),
                    "response", items
            );

        }


        return null;
    }


    public record TaskRequestDto(Long taskId,
                                 TaskType taskType,
                                 TaskStatus taskStatus,
                                 Map<String, Object> input) {
        public static TaskRequestDto of(Task task) {
            return new TaskRequestDto(
                    task.getTaskId(),
                    task.getTaskType(),
                    task.getStatus(),
                    task.getInput()
            );
        }
    }


}
