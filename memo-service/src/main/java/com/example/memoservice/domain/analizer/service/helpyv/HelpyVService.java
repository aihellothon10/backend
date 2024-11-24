package com.example.memoservice.domain.analizer.service.helpyv;

import com.example.memoservice.domain.analizer.TaskCommandService;
import com.example.memoservice.domain.analizer.TaskQueryService;
import com.example.memoservice.domain.analizer.model.Job;
import com.example.memoservice.domain.analizer.model.TaskStatus;
import com.example.memoservice.domain.analizer.model.TaskType;
import com.example.memoservice.domain.analizer.service.AnalyzerService;
import com.example.memoservice.domain.analizer.service.DataMapper;
import com.example.memoservice.domain.apiclient.client.dto.Role;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatClient;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatRequest;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.memoservice.config.Constants.IMAGE_BASE_URL;
import static com.example.memoservice.domain.analizer.service.helpyv.HelpyVSystemPrompt.BASE_PROMPT;

@Service
@Slf4j
public class HelpyVService implements AnalyzerService {

    private final HelpyVChatClient client;
    private final HelpyVSystemPromptProperty property;
    //    private final HelpyVChatRequest.MessageDto systemPrompt;
    private final TaskQueryService taskQueryService;
    private final TaskCommandService taskCommandService;


    public HelpyVService(HelpyVChatClient client, HelpyVSystemPromptProperty property, TaskQueryService taskQueryService, TaskCommandService taskCommandService) {
        this.client = client;
        this.property = property;
//        this.systemPrompt = createSystemPromptFromProperty();
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

//         사용자가 입력한 내용 + (Optional) diffbot에서 가져온 내용들
//        HelpyVInput input = DataMapper.toObjectWithMapper(currentTask.getInput(), HelpyVInput.class);


//        String query = input.getQuery();
//        String query = currentTask.getJob().getAnalyzeInput().getQuery();
//        List<String> crawlingContents = input.getCrawlingContents();
//
//        // 사용자입력내용 + 크롤링데이터를 같이 넣음
//        List<String> queries = new ArrayList<>();
//        queries.add(query);
//        if (!CollectionUtils.isEmpty(crawlingContents)) {
//            queries.addAll(crawlingContents);
//        }
//
//        HelpyVChatRequest request = HelpyVChatRequest.of(
//                List.of(createSystemPrompt(), createUserPrompt(queries))
//        );
        /////////////////////////////////
        // input에 request를 직접 넣는다.
        var request = DataMapper.toObjectWithMapper(currentTask.getInput(), HelpyVChatRequest.class);

        List<String> images = currentTask.getJob().getAnalyzeInput().getImages();
//        if (!CollectionUtils.isEmpty(images)) {
        if (!images.isEmpty() && StringUtils.hasText(images.getFirst())) {
            List<HelpyVChatRequest.Content> imageUrls = images.stream()
                    .map(i -> HelpyVChatRequest.Content.ofImage(IMAGE_BASE_URL + i))
                    .toList();
            HelpyVChatRequest.Content imageQuery = HelpyVChatRequest.Content.ofText(
                    "이 사진들을 답변에 참고해주세요."
            );
            List<HelpyVChatRequest.Content> imageContents = new ArrayList<>(imageUrls);
            imageContents.add(imageQuery);
            HelpyVChatRequest.MessageDto imageMessage = new HelpyVChatRequest.MessageDto(
                    Role.user, imageContents
            );
            request.getMessages().add(imageMessage);
        }


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

        Job job = currentTask.getJob();
        job.setHelpyVResult(
                content.toString(),
                content.getQuestionTitle(),
                content.getAnswer(),
                content.getContentContainBoolean(),
                content.getContentContainBooleanExplain()
        );

        var nextTask = taskQueryService.getNextTask(currentTask.getJob().getJobId(), currentTaskId);
        if (nextTask == null) {
            return null;
        }

        if (nextTask.getTaskType() == TaskType.Perplexity) {
            // Deprecated
//            // helpyV가 분석한 결과를 전달
//            // TODO 일단 NaverSearch에서 사용할 키워드를 Perplexity에 전달한다.
//            nextTask.setInput(Map.of(
//                    "answer", content.getAnswer(),
//                    "keyword", content.getKeyword()
//            ));

        } else if (nextTask.getTaskType() == TaskType.NaverSearch) {
            // Deprecated
//            NaverSearchInput nextInput = new NaverSearchInput();
//            nextInput.setKeywords(content.getBooks().stream()
//                    .map(HelpyVChatResponse.Book::getTitle)
//                    .toList());
//            nextTask.setInput(DataMapper.toMapWithMapper(nextInput));
        } else if (nextTask.getTaskType() == TaskType.ToxicityPrediction) {
            String query = currentTask.getJob().getAnalyzeInput().getQuery();
            List<String> crawlingContents = currentTask.getJob().getAnalyzeResult().getCrawlingContents();

            Map<String, Object> nextInput = new HashMap<>();
            nextInput.put("answer", content.getAnswer());
            nextInput.put("keyword", content.getKeyword());
            nextInput.put("questionTitle", content.getQuestionTitle());
            nextInput.put("coreContentSummary", content.getCoreContentSummary());
            // 입력을 유해성 검사하기위해 전달
            nextInput.put("query", query);
            if (!CollectionUtils.isEmpty(crawlingContents)) {
                nextInput.put("crawlingContents", crawlingContents);
            }
            nextTask.setInput(nextInput);
        } else {
            return null;
        }

//        nextTask.setInput(Map.of("query", response.getChoices().getFirst().getMessage().getContent().getAnswer()));
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
        return BASE_PROMPT.toString();
    }

    @Deprecated
    private HelpyVChatRequest.MessageDto createSystemPromptFromProperty() {
        var systemPrompts = property.getSystemPrompt().stream()
                .map(p -> HelpyVChatRequest.Content.ofText(p.getValue()))
                .toList();
        return new HelpyVChatRequest.MessageDto(Role.system, systemPrompts);
    }

    public static HelpyVChatRequest createRequest(List<String> queries) {
        return HelpyVChatRequest.of(
                List.of(createSystemPrompt(), createUserPrompt(queries))
        );
    }


    private static HelpyVChatRequest.MessageDto createSystemPrompt() {
//        var systemPrompts = property.getSystemPrompt().stream()
//                .map(p -> HelpyVChatRequest.Content.of(p.getValue()))
//                .toList();
        var systemPrompts = BASE_PROMPT.stream()
                .map(HelpyVChatRequest.Content::ofText)
                .toList();
        return new HelpyVChatRequest.MessageDto(Role.system, systemPrompts);
    }

    private HelpyVChatRequest.MessageDto createUserPrompt(String query) {
        return new HelpyVChatRequest.MessageDto(Role.user, List.of(HelpyVChatRequest.Content.ofText(query)));
    }

    public static HelpyVChatRequest.MessageDto createUserPrompt(List<String> queries) {
        return new HelpyVChatRequest.MessageDto(Role.user,
                queries.stream()
                        .map(HelpyVChatRequest.Content::ofText)
                        .toList());
    }

}
