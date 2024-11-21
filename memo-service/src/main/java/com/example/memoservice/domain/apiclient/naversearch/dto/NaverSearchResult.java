package com.example.memoservice.domain.apiclient.naversearch.dto;


import java.util.List;

public record NaverSearchResult(String lastBuildDate,
                                int total,
                                int start,
                                int display,
                                List<Item> items) {

    public record Item(String title,
                       String link,
                       String description) {
    }
}
