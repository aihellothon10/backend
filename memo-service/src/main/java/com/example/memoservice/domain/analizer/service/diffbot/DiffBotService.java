package com.example.memoservice.domain.analizer.service.diffbot;

import com.example.memoservice.domain.analizer.TaskCommandService;
import com.example.memoservice.domain.analizer.TaskQueryService;
import com.example.memoservice.domain.analizer.model.TaskStatus;
import com.example.memoservice.domain.analizer.model.TaskType;
import com.example.memoservice.domain.analizer.service.AnalyzerService;
import com.example.memoservice.domain.analizer.service.DataMapper;
import com.example.memoservice.domain.apiclient.diffbot.DiffbotApiClient;
import com.example.memoservice.domain.apiclient.diffbot.dto.DiffbotResponse;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.memoservice.domain.analizer.service.helpyv.HelpyVService.createUserPrompt;

@Service
@RequiredArgsConstructor
public class DiffBotService implements AnalyzerService {

    private final DiffbotApiClient client;
    private final TaskQueryService taskQueryService;
    private final TaskCommandService taskCommandService;


    @Override
    public Long analyze(Long currentTaskId) {
        // TODO diffbot은 Job에서 직접 링크를 가져온다...
        var currentTask = taskQueryService.getTaskByTaskId(currentTaskId);

        String query = currentTask.getJob().getAnalyzeInput().getQuery();
        List<String> links = currentTask.getJob().getAnalyzeInput().getLinks();

        List<DiffbotResponse> responses = new ArrayList<>();
        links.forEach(link -> {
            var response = client.crawlPage(link);
            responses.add(response);
        });

        taskCommandService.setRequestResponse(
                currentTask,
                Map.of("links", links),
                Map.of("responses", responses)
        );
        List<String> crawlingContents = responses.stream()
                .map(r -> r.getObjects().getFirst().getText())
                .toList();
        List<String> crawlingImages = responses.stream()
                .map(r -> r.getObjects().getFirst().getImages())
                .flatMap(List::stream)
                .map(DiffbotResponse.ImageInfo::getUrl)
                .toList();
        currentTask.getJob().setDiffBotResult(crawlingContents, crawlingImages);

        currentTask.updateStatus(TaskStatus.Completed);

        var nextTask = taskQueryService.getNextTask(currentTask.getJob().getJobId(), currentTaskId);
        if (nextTask == null) {
            return null;
        }
        if (nextTask.getTaskType() == TaskType.HelpyV) {
            // 사용자 입력 + 추출한 내용들

//            nextTask.setInput(Map.of(
//                    "crawlingContents", crawlingContents
//            ));
            List<String> queries = new ArrayList<>();
            queries.add(query);
            queries.addAll(crawlingContents);

            var request = DataMapper.toObjectWithMapper(currentTask.getInput(), HelpyVChatRequest.class);
            request.getMessages().add(createUserPrompt(queries));


            nextTask.setInput(
                    DataMapper.toMapWithMapper(request)
            );


        } else if (nextTask.getTaskType() == TaskType.ToxicityPrediction) {
            // Deprecated
//            var query = currentTask.getJob().getAnalyzeInput().getQuery();
//            // 유해성 검사를 할 항목들... 일단 두가지 넣음
//
//            // 유해성 검사의 입력의 최대 길이가 1000글자라서 더 긴 문자여리은 자른다.
//            List<String> crawlingContents = responses.stream()
//                    .map(r -> {
//                        String text = r.getObjects().getFirst().getText();
//                        return text.length() > 500 ? text.substring(0, 500) : text;
//                    })
//                    .toList();
//
//            nextTask.setInput(Map.of(
//                    "query", query, // 사용자 입력
//                    "crawlingContents", crawlingContents
//            ));
        } else {

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
