package com.example.memoservice.domain.analizer.service.perplexcity;

import com.example.memoservice.domain.analizer.TaskCommandService;
import com.example.memoservice.domain.analizer.TaskQueryService;
import com.example.memoservice.domain.analizer.service.AnalyzerService;
import com.example.memoservice.domain.analizer.service.DataMapper;
import com.example.memoservice.domain.apiclient.client.dto.Role;
import com.example.memoservice.domain.apiclient.perplexity.PerplexityClient;
import com.example.memoservice.domain.apiclient.perplexity.dto.PerplexityRequest;
import com.example.memoservice.domain.apiclient.perplexity.dto.PerplexityResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PerPlexityService implements AnalyzerService {

    private final PerplexityClient perplexityClient;
    private final PerPlexitySystemPromptProperty property;
    private final List<PerplexityRequest.Message> systemPrompt;
    private final TaskQueryService taskQueryService;
    private final TaskCommandService taskCommandService;


    public PerPlexityService(PerplexityClient perplexityClient, PerPlexitySystemPromptProperty property, TaskQueryService taskQueryService, TaskCommandService taskCommandService) {
        this.perplexityClient = perplexityClient;
        this.property = property;
        this.taskQueryService = taskQueryService;
        this.taskCommandService = taskCommandService;
        this.systemPrompt = createSystemPrompt();
    }


    @Override
    public Long analyze(Long currentTaskId) {
        var currentTask = taskQueryService.getTaskByTaskId(currentTaskId);

        var inputData = currentTask.getInput();


        List<PerplexityRequest.Message> messages = new ArrayList<>(systemPrompt);
        messages.add(createUserPrompt(inputData.get("query").toString()));
        PerplexityRequest request = PerplexityRequest.of(
                messages
        );
        var response = callApi(request);

        taskCommandService.setRequestResponse(currentTask, DataMapper.toMapWithMapper(request), DataMapper.toMapWithMapper(response));

        // TODO 임시로 결과 텍스트 전체를 결과로 저장
        Map<String, Object> result = Map.of("answer", response.choices().getFirst().message().content());
        currentTask.setResult(result);

        var nextTask = taskQueryService.getNextTask(currentTask.getJob().getJobId(), currentTaskId);
        if (nextTask == null) {
            return null;
        }

        var nextInput = response.citations();
        nextTask.setInput(Map.of("query", nextInput));
        return nextTask.getTaskId();
    }

    @Override
    public boolean hasPrompt() {
        return true;
    }

    @Override
    public String getSystemPrompt() {
        return systemPrompt.toString();
    }

    public PerplexityResponse callApi(PerplexityRequest request) {
        return perplexityClient.chat(request);
    }

    private List<PerplexityRequest.Message> createSystemPrompt() {
        return property.getSystemPrompt().stream()
                .map(p -> PerplexityRequest.Message.of(Role.system, p.getValue()))
                .toList();
    }

    private PerplexityRequest.Message createUserPrompt(String query) {
        return new PerplexityRequest.Message(Role.user, query);
    }

}
