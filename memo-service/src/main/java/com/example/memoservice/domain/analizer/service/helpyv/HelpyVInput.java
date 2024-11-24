package com.example.memoservice.domain.analizer.service.helpyv;

import lombok.Data;

import java.util.List;

/**
 * 문자열을 입력받아 분석 수행
 */
@Data
public class HelpyVInput {
    private String query; // 사용자 입력
    private List<String> crawlingContents; // diffbot으로 분석한 결과들
}
