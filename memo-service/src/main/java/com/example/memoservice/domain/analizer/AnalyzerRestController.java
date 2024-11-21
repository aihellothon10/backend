package com.example.memoservice.domain.analizer;

import com.example.memoservice.domain.analizer.model.Job;
import com.example.memoservice.domain.analizer.model.JobType;
import com.example.memoservice.domain.analizer.service.helpyv.HelpyVService;
import com.example.memoservice.domain.analizer.service.naversearch.NaverSearchService;
import com.example.memoservice.domain.analizer.service.perplexcity.PerPlexityService;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatRequest;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatResponse;
import com.example.memoservice.domain.apiclient.naversearch.dto.NaverSearchResult;
import com.example.memoservice.domain.apiclient.perplexity.dto.PerplexityRequest;
import com.example.memoservice.domain.apiclient.perplexity.dto.PerplexityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analyzers")
class AnalyzerRestController {

    private final AnalyzeCommandService analyzeCommandService;
    private final JobQueryService jobQueryService;

    private final HelpyVService heppyVService;
    private final PerPlexityService perPlexityService;
    private final NaverSearchService naverSearchService;

    /**
     * 메모 생성을 위한 분석 요청
     */
    @PostMapping("/memos")
    JobIdResponse requestMemo(@RequestBody AnalyzeRequest request) {
        return new JobIdResponse(analyzeCommandService.requestAnalyze(request, JobType.Memo));
    }

    @PostMapping("/questions")
    JobIdResponse requestQuestion(@RequestBody AnalyzeRequest request) {
        return new JobIdResponse(analyzeCommandService.requestAnalyze(request, JobType.Question));
    }

    // TODO 메모 생성에 필요한 정보들을 반환할 예정
    @GetMapping("/{jobId}")
    JobResponse getAnalyzeResult(@PathVariable Long jobId) {
        return JobResponse.of(jobQueryService.getJob(jobId));
    }

    record JobIdResponse(Long jobId) {
    }

    record JobResponse(String name) {
        public static JobResponse of(Job job) {
            return new JobResponse(job.getName());
        }
    }

    //=================================== TEST ===================================
    // feinclient 직접 호출
    @PostMapping("/helpyv/test")
    HelpyVChatResponse addHelpyV(@RequestBody HelpyVChatRequest request) {
        return heppyVService.callApi(request);
    }

    // feinclient 직접 호출
    @PostMapping("/perplexity/test")
    PerplexityResponse callApi(@RequestBody PerplexityRequest request) {
        return perPlexityService.callApi(request);
    }

    // feinclient 직접 호출
    @GetMapping("/naversearch/test")
    NaverSearchResult callApi(@RequestParam String query,
                              @RequestParam(required = false, defaultValue = "1") int start,
                              @RequestParam(required = false, defaultValue = "10") int display) {
        return naverSearchService.callApi(query, start, display);
    }

}
