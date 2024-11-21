package com.example.memoservice.domain.analizer.service.naversearch;

import com.example.memoservice.domain.analizer.TaskCommandService;
import com.example.memoservice.domain.analizer.TaskQueryService;
import com.example.memoservice.domain.analizer.service.AnalyzerService;
import com.example.memoservice.domain.analizer.service.DataMapper;
import com.example.memoservice.domain.apiclient.naversearch.NaverSearchClient;
import com.example.memoservice.domain.apiclient.naversearch.dto.NaverSearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverSearchService implements AnalyzerService {

    private final NaverSearchClient client;

    private final TaskQueryService taskQueryService;
    private final TaskCommandService taskCommandService;

    private static final int START = 1;
    private static final int DISPLAY = 3;


    @Override
    public Long analyze(Long currentTaskId) {
        var currentTask = taskQueryService.getTaskByTaskId(currentTaskId);


        var inputData = currentTask.getInput();

        NaverSearchInput input = DataMapper.toObjectWithMapper(inputData, NaverSearchInput.class);
        List<String> keywords = input.getKeywords();
        List<List<NaverSearchResult.Item>> results = new ArrayList<>();
        List<NaverSearchResult> responses = new ArrayList<>();
        // inputData에서 kewords(String 배열)을 받아서 각각 키워드 검색
        keywords.forEach(keyword -> {
            var response = callApi(keyword, START, DISPLAY);
            responses.add(response);
            results.add(response.items());
        });


        taskCommandService.setRequestResponse(
                currentTask,
                Map.of("keywords", keywords),
                Map.of("responses", responses)
        );

        // TODO 임시로 결과 텍스트 전체를 결과로 저장
        currentTask.setResult(Map.of("responses", results));

        var nextTask = taskQueryService.getNextTask(currentTask.getJob().getJobId(), currentTaskId);
        if (nextTask == null) {
            return null;
        }

        // TODO 다음 Task의 입력데이터 설정
        nextTask.setInput(Map.of("query", keywords));
        return nextTask.getTaskId();
    }

    public NaverSearchResult callApi(String query, int start, int display) {
        return client.search(query, start, display);
    }

    @Override
    public boolean hasPrompt() {
        return false;
    }

    @Override
    public String getSystemPrompt() {
        return null;
    }

}
