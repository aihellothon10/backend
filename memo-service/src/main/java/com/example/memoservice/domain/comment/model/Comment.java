package com.example.memoservice.domain.comment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    // TODO 작성자
//    private Long memberId;

    private Long memoId;

    private String content;


    public Comment(Long memoId, String content) {
        this.memoId = memoId;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }

}
