package com.example.memoservice.domain.analizer.model;

import com.example.memoservice.config.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AnalyzeInput {
    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> images = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String query;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> links;

    public AnalyzeInput(List<String> images, String query, List<String> links) {
        this.images = images;
        this.query = query;
        this.links = links;
    }

}
