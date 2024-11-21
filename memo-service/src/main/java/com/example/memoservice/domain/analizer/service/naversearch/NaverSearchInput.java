package com.example.memoservice.domain.analizer.service.naversearch;

import lombok.Data;

import java.util.List;

/**
 * 검색할 단어들 목록
 */
@Data
public class NaverSearchInput {
    List<String> keywords;
}
