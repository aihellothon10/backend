package com.example.memoservice.domain.memo.respository;

import com.example.memoservice.domain.memo.model.Memo;
import lombok.Data;

import java.util.List;

@Data
public class MemoListDto {

    private Long memoId;
    private String query;
    private String generatedTitle;
    private List<String> images;
    private List<String> links;
    private List<String> targetMembers;
    private List<String> targetBabies;
    private List<String> targetAudiences;

    public static MemoListDto of(Memo memo) {
        MemoListDto memoListDto = new MemoListDto();
        memoListDto.setMemoId(memo.getMemoId());
        memoListDto.setQuery(memo.getAnalyzeInput().getQuery());
        memoListDto.setGeneratedTitle(memo.getAnalyzeResult().getGeneratedTitle());
        memoListDto.setImages(memo.getAnalyzeInput().getImages());
        memoListDto.setLinks(memo.getAnalyzeInput().getLinks());
        memoListDto.setTargetMembers(memo.getTargetMembers());
        memoListDto.setTargetBabies(memo.getTargetBabies());
        memoListDto.setTargetAudiences(memo.getTargetAudiences());
        return memoListDto;
    }

}
