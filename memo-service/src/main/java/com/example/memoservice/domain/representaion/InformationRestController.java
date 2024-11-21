package com.example.memoservice.domain.representaion;

import com.example.memoservice.domain.information.CreateInformationRequest;
import com.example.memoservice.domain.information.InformationCommandService;
import com.example.memoservice.domain.information.InformationQueryService;
import com.example.memoservice.domain.information.model.Information;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/infos")
public class InformationRestController {

    private final InformationCommandService informationCommandService;
    private final InformationQueryService informationQueryService;


    @PostMapping
    InformationIdResponse createInformation(@RequestBody CreateInformationRequest request) {
        return new InformationIdResponse(informationCommandService.createInformation(request));
    }

    @PostMapping("/bulk")
    void createInformationBulk(@RequestBody List<CreateInformationRequest> request) {
        informationCommandService.createInformationBulk(request);
    }

    @GetMapping("/{informationId}")
    Information getInformation(@PathVariable Long informationId) {
        return informationQueryService.getInformation(informationId);
    }

    @GetMapping
    List<Information> getInformations() {
        return informationQueryService.getAllInformation();
    }


    record InformationIdResponse(Long informationId) {
    }

}
