package com.example.memoservice.domain.analizer.service.helpyv;

import com.example.memoservice.domain.analizer.TaskCommandService;
import com.example.memoservice.domain.analizer.TaskQueryService;
import com.example.memoservice.domain.analizer.model.TaskStatus;
import com.example.memoservice.domain.analizer.model.TaskType;
import com.example.memoservice.domain.analizer.service.AnalyzerService;
import com.example.memoservice.domain.analizer.service.DataMapper;
import com.example.memoservice.domain.analizer.service.naversearch.NaverSearchInput;
import com.example.memoservice.domain.apiclient.client.dto.Role;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatClient;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatRequest;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HelpyVService implements AnalyzerService {

    private final HelpyVChatClient client;
    private final HelpyVSystemPromptProperty property;
    private final HelpyVChatRequest.MessageDto systemPrompt;
    private final TaskQueryService taskQueryService;
    private final TaskCommandService taskCommandService;

    public HelpyVService(HelpyVChatClient client, HelpyVSystemPromptProperty property, TaskQueryService taskQueryService, TaskCommandService taskCommandService) {
        this.client = client;
        this.property = property;
        this.systemPrompt = createSystemPromptFromProperty();
        this.taskQueryService = taskQueryService;
        this.taskCommandService = taskCommandService;
    }

    @Data
    public static class HelpyInputData {

    }

    @Data
    public static class HelpyOutputData {

    }


    @Override
    public Long analyze(Long currentTaskId) {
        var currentTask = taskQueryService.getTaskByTaskId(currentTaskId);

        HelpyVInput input = DataMapper.toObjectWithMapper(currentTask.getInput(), HelpyVInput.class);

        String query = input.getQuery();
        HelpyVChatRequest request = HelpyVChatRequest.of(
                List.of(systemPrompt, createUserPrompt(query))
        );
        var response = client.chat(request);

        taskCommandService.setRequestResponse(currentTask, DataMapper.toMapWithMapper(request), DataMapper.toMapWithMapper(response));

        //////////////////////////
        currentTask.updateStatus(TaskStatus.Completed);

        // TODO 현재 모델의 결과를 가공한 해서 다음 태스크의 입력에 넣어야한다.
        /** 결과 가공
         * - 제목(question_title)
         * - 요약문(answer)
         * - 진위여부(content_contain_boolean)
         * - 문헌 목록(books)
         * - 판단결과(content_contain_boolean_explain)
         * ---
         * - input에서 가져오는거
         * - 원문(inputData.getQuery())
         */

        var content = response.getChoices().getFirst().getMessage().getContent();

        currentTask.setResult(DataMapper.toMapWithMapper(content));

        var nextTask = taskQueryService.getNextTask(currentTask.getJob().getJobId(), currentTaskId);
        if (nextTask == null) {
            return null;
        }

        if (nextTask.getTaskType() == TaskType.NaverSearch) {
            NaverSearchInput nextInput = new NaverSearchInput();
            nextInput.setKeywords(content.getBooks().stream()
                    .map(HelpyVChatResponse.Book::getTitle)
                    .toList());
            nextTask.setInput(DataMapper.toMapWithMapper(nextInput));
        }

        nextTask.setInput(Map.of("query", response.getChoices().getFirst().getMessage().getContent().getAnswer()));
        return nextTask.getTaskId();
    }

    /**
     * 외부 API 호출과 동일한 형식이다.
     */
    public HelpyVChatResponse callApi(HelpyVChatRequest request) {
        return client.chat(request);
    }

    @Override
    public boolean hasPrompt() {
        return true;
    }

    @Override
    public String getSystemPrompt() {
        return systemPrompt.toString();
    }

    private HelpyVChatRequest.MessageDto createSystemPromptFromProperty() {
        var systemPrompts = property.getSystemPrompt().stream()
                .map(p -> HelpyVChatRequest.Content.of(p.getValue()))
                .toList();
        return new HelpyVChatRequest.MessageDto(Role.system, systemPrompts);
    }

    private HelpyVChatRequest.MessageDto createUserPrompt(String query) {
        return new HelpyVChatRequest.MessageDto(Role.user, List.of(HelpyVChatRequest.Content.of(query)));
    }

}
