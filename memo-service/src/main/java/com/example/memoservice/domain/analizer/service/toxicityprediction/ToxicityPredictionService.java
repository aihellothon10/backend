package com.example.memoservice.domain.analizer.service.toxicityprediction;

import com.example.memoservice.domain.analizer.TaskCommandService;
import com.example.memoservice.domain.analizer.TaskQueryService;
import com.example.memoservice.domain.analizer.model.TaskType;
import com.example.memoservice.domain.analizer.service.AnalyzerService;
import com.example.memoservice.domain.analizer.service.DataMapper;
import com.example.memoservice.domain.apiclient.elice.ToxicityPredictionClient;
import com.example.memoservice.domain.apiclient.elice.ToxicityPredictionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ToxicityPredictionService implements AnalyzerService {

    private final ToxicityPredictionClient client;

    private final TaskQueryService taskQueryService;
    private final TaskCommandService taskCommandService;


    // TODO INPUT / OUTPUT 데이터 설정 필요...
    @Override
    public Long analyze(Long currentTaskId) {
        var currentTask = taskQueryService.getTaskByTaskId(currentTaskId);

        // inputData query(String), crawlingContents(List<String>)
        var inputData = currentTask.getInput();

        List<String> texts = new ArrayList<>();
        String answer = (String) inputData.get("answer");
        texts.add(answer);
        String keyword = (String) inputData.get("keyword");
        texts.add(keyword);
        String questionTitle = (String) inputData.get("questionTitle");
        texts.add(questionTitle);
        String coreContentSummary = (String) inputData.get("coreContentSummary");
        texts.add(coreContentSummary);
        String query = (String) inputData.get("query");
        texts.add(query);
        List<String> crawlingContents = (List<String>) inputData.get("crawlingContents");
        if (!CollectionUtils.isEmpty(crawlingContents)) {
            // 유해성 검사의 입력의 최대 길이가 1000글자라서 자른다.
            crawlingContents = crawlingContents.stream()
                    .map(s -> s.length() > 500 ? s.substring(0, 500) : s)
                    .toList();
            texts.addAll(crawlingContents);
        }

        ToxicityPredictionRequest request = new ToxicityPredictionRequest(texts);
        var response = client.check(request);

        taskCommandService.setRequestResponse(
                currentTask,
                DataMapper.toMapWithMapper(request),
                Map.of("response", response)
        );
        currentTask.getJob().setToxicityResult(
                texts, response
        );

        var nextTask = taskQueryService.getNextTask(currentTask.getJob().getJobId(), currentTaskId);
        if (nextTask == null) {
            return null;
        }

        // Deprecated
//        if (nextTask.getTaskType() == TaskType.HelpyV) {
//            HelpyVInput nextInput = new HelpyVInput();
//            nextInput.setQuery((String) inputData.get("query"));
//            nextInput.setCrawlingContent((String) inputData.get("crawlingContents"));
//            nextTask.setInput(DataMapper.toMapWithMapper(nextInput));
//        }
        if (nextTask.getTaskType() == TaskType.Perplexity) {

            nextTask.setInput(Map.of(
                    "answer", answer, // perplexity에서 분석
                    "keyword", keyword)); // naver 검색에 사용
        } else {
            return null;
        }

        return nextTask.getTaskId();
    }

    @Override
    public boolean hasPrompt() {
        return false;
    }

    @Override
    public String getSystemPrompt() {
        return "";
    }
}
