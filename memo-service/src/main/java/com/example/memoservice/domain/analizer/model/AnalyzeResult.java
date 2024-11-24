package com.example.memoservice.domain.analizer.model;

import com.example.memoservice.config.NaverSearchResultItemListConverter;
import com.example.memoservice.config.StringListConverter;
import com.example.memoservice.config.ToxicityPredictionResponseListConverter;
import com.example.memoservice.domain.apiclient.elice.ToxicityPredictionResponse;
import com.example.memoservice.domain.apiclient.naversearch.dto.NaverSearchResult;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AnalyzeResult {

    //============== HelpyV =====================
    @Column(columnDefinition = "TEXT")
    private String assistantContent;

    @Column(columnDefinition = "TEXT")
    private String generatedTitle = null;

    @Column(columnDefinition = "TEXT")
    private String answer = null;

    private Integer contentContainBoolean = null;

    @Column(columnDefinition = "TEXT")
    private String contentContainBooleanExplain = null;

    //============== DiffBot =====================
    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> crawlingContents = new ArrayList<>();

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> crawlingImages = new ArrayList<>();

    //============== Toxicity =====================
    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> toxicityTexts = new ArrayList<>();

    @Convert(converter = ToxicityPredictionResponseListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<ToxicityPredictionResponse> toxicityPredictions = new ArrayList<>();

    //============== Perplexity =====================
    @Column(columnDefinition = "TEXT")
    private String content;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> citations = new ArrayList<>();

    //============== Naver Search =====================
    @Column(columnDefinition = "TEXT")
    private String keyword;

    @Convert(converter = NaverSearchResultItemListConverter.class)
    @Column(columnDefinition = "TEXT") // JSON 데이터 저장
    private List<NaverSearchResult.Item> searchResults = new ArrayList<>();
    

    public void setDiffBotResult(List<String> crawlingContents,
                                 List<String> crawlingImages) {
        this.crawlingContents = crawlingContents;
        this.crawlingImages = crawlingImages;
    }

    public void setHelpyVResult(String assistantContent,
                                String generatedTitle,
                                String answer,
                                Integer contentContainBoolean,
                                String contentContainBooleanExplain) {
        this.assistantContent = assistantContent;
        this.generatedTitle = generatedTitle;
        this.answer = answer;
        this.contentContainBoolean = contentContainBoolean;
        this.contentContainBooleanExplain = contentContainBooleanExplain;
    }

    public void setToxicityResult(List<String> toxicityTexts,
                                  List<ToxicityPredictionResponse> toxicityPredictions) {
        this.toxicityTexts = toxicityTexts;
        this.toxicityPredictions = toxicityPredictions;
    }

    public void setPerplexityResult(String content,
                                    List<String> citations) {
        this.content = content;
        this.citations = citations;
    }

    public void setNaverSearchResult(String keyword,
                                     List<NaverSearchResult.Item> searchResults) {
        this.keyword = keyword;
        this.searchResults = searchResults;
    }

}

