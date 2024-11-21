package com.example.memoservice.domain.information.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 전문가 정보를 저장
 */
@Entity
@Getter
@NoArgsConstructor
public class Information {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long informationId;

    @Column(name = "document_id", nullable = false, unique = true, updatable = false, length = 40)
    private String documentId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;


    public Information(String documentId, String title, String content) {
        this.documentId = documentId;
        this.title = title;
        this.content = content;
    }

}
