package com.example.memoservice.domain.analizer;

import com.example.memoservice.domain.analizer.model.*;
import com.example.memoservice.domain.analizer.service.helpyv.HelpyVService;
import com.example.memoservice.domain.analizer.service.naversearch.NaverSearchService;
import com.example.memoservice.domain.analizer.service.perplexcity.PerPlexityService;
import com.example.memoservice.domain.apiclient.diffbot.DiffbotApiClient;
import com.example.memoservice.domain.apiclient.elice.ToxicityPredictionClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analyzers")
class AnalyzerRestController {

    private final AnalyzeCommandService analyzeCommandService;
    private final JobQueryService jobQueryService;

    private final HelpyVService heppyVService;
    private final PerPlexityService perPlexityService;
    private final NaverSearchService naverSearchService;
    private final DiffbotApiClient diffbotApiClient;
    private final ToxicityPredictionClient toxicityPredictionClient;
    private final TaskQueryService taskQueryService;


    //    @PostMapping(value = "/memos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    JobIdResponse requestMemo(@RequestPart AnalyzeRequest request,
//                              @RequestPart("images") List<MultipartFile> images) {
//        return new JobIdResponse(analyzeCommandService.requestAnalyze(request, images));
//    }
//
//    //    @PostMapping("/questions")
//    @PostMapping(value = "/questions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    JobIdResponse requestQuestion(@RequestPart AnalyzeRequest request,
//                                  @RequestPart("images") List<MultipartFile> images) {
//        return new JobIdResponse(analyzeCommandService.requestAnalyze(request, images));
//    }
//
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JobIdResponse requestAnalyze(@RequestPart AnalyzeRequest request,
                                 @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return new JobIdResponse(analyzeCommandService.requestAnalyze(request, images));
    }

    @PostMapping(value = "/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JobIdResponse requestAnalyzeForm(@ModelAttribute AnalyzeRequestForm request) {
        return new JobIdResponse(analyzeCommandService.requestAnalyze(new AnalyzeRequest(
                request.getQuery(),
                request.getJobType(),
                request.getLinks()
        ), request.getImages()));
    }

    // TODO 메모 생성에 필요한 정보들을 반환할 예정
    @GetMapping("/{jobId}")
    JobResponse getAnalyzeResult(@PathVariable Long jobId) {
        return JobResponse.of(jobQueryService.getJob(jobId));
    }

    record JobIdResponse(Long jobId) {
    }

    record JobResponse(AnalysisStatus status,
                       AnalyzeInput analyzeInput,
                       AnalyzeResult analyzeResult) {
        public static JobResponse of(Job job) {
            return new JobResponse(
                    job.getStatus(),
                    job.getAnalyzeInput(),
                    job.getAnalyzeResult()
            );
        }
    }

    //=================================== Task 정보 ===================================

    /**
     * taskId에 해당하는 Task가 taskType과 다를경우 오류 발생
     */
    @GetMapping("/tasks/{taskId}/type/{taskType}")
    Map<String, Object> geTaskDetails(@PathVariable Long taskId,
                                      @PathVariable TaskType taskType) {
        return taskQueryService.getTaskByTaskIdWithTaskType(taskId, taskType);
    }


    //=================================== TEST ===================================
//    // feinclient 직접 호출
//    @PostMapping("/helpyv/test")
//    HelpyVChatResponse addHelpyV(@RequestBody HelpyVChatRequest request) {
//        return heppyVService.callApi(request);
//    }
//
//    // feinclient 직접 호출
//    @PostMapping("/perplexity/test")
//    PerplexityResponse callApi(@RequestBody PerplexityRequest request) {
//        return perPlexityService.callApi(request);
//    }
//
//    // feinclient 직접 호출
//    @GetMapping("/naversearch/test")
//    NaverSearchResult callApi(@RequestParam String query,
//                              @RequestParam(required = false, defaultValue = "1") int start,
//                              @RequestParam(required = false, defaultValue = "10") int display) {
//        return naverSearchService.callApi(query, start, display);
//    }

//    @GetMapping("/diffbot/test")
//    DiffbotResponse callApi(@RequestParam String url) {
//        return diffbotApiClient.crawlPage(url);
//    }
//
//    @PostMapping("/toxicity/test")
//    List<ToxicityPredictionResponse> callApi(@RequestBody ToxicityPredictionRequest request) {
//        return toxicityPredictionClient.check(request);
//    }

}
