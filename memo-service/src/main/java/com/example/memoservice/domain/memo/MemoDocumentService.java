package com.example.memoservice.domain.memo;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.memo.model.Memo;
import com.example.memoservice.domain.memo.respository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoDocumentService {

    private final MemoRepository memoRepository;
    private final VectorStore vectorStore;


    /**
     * 메모와 함께 Document 생성
     * - 주의 : Document의 내용은 수정 불가
     */
    public Long createMemo(MemoCreateRequest dto) {

        // TODO 메모를 적절한 형태로 가공(content, metadata 등...)
//        Document document = new Document("title : %s, content : %s".formatted(dto.title(), dto.content()));
//        vectorStore.add(List.of(document));

        Memo newMemo = Memo.builder()
                .documentId("document.getId()")
                .title(dto.title())
                .content(dto.content())
                .evaluationStatus(dto.evaluationStatus())
                .targetAudience(dto.targetAudience())
                .userInput(dto.userInput())
                .analysisSummary(dto.analysisSummary())
                .source(dto.source())
                .build();
        Memo savedMemo = memoRepository.save(newMemo);
        return savedMemo.getMemoId();
    }

    public void deleteMemo(Long memoId) {
        Memo memo = findMemo(memoId);

        vectorStore.delete(List.of(memo.getDocumentId()));
        memoRepository.delete(memo);
    }

    private Memo findMemo(Long memoId) {
        return memoRepository.findById(memoId)
                .orElseThrow(() -> new DataNotFoundException("Memo Not Found"));
    }

}
