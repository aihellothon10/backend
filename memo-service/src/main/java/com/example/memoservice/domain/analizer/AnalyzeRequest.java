package com.example.memoservice.domain.analizer;

import com.example.memoservice.domain.analizer.model.JobType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// TODO 메모, 링크, 이미지 등등 포함될 예정
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzeRequest {
    private String query;
    private JobType jobType; // Memo, Question
    private List<String> links;
}
