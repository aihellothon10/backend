package com.example.memoservice.domain.memo;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.analizer.JobQueryService;
import com.example.memoservice.domain.analizer.model.Job;
import com.example.memoservice.domain.memo.model.Memo;
import com.example.memoservice.domain.memo.respository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoDocumentService {

    private final MemoRepository memoRepository;
    private final VectorStore vectorStore;

    private final JobQueryService jobQueryService;

    /**
     * 메모와 함께 Document 생성
     * - 주의 : Document의 내용은 수정 불가
     */
    public Long createMemo(MemoCreateRequest dto) {

        // TODO 메모를 적절한 형태로 가공(content, metadata 등...)
//        Document document = new Document("title : %s, content : %s".formatted(dto.title(), dto.content()));
//        vectorStore.add(List.of(document));

        Job job = jobQueryService.getJob(dto.jobId());

        Memo newMemo = Memo.builder()
                .jobId(dto.jobId())
                .documentId("document.getId()")
                .targetBabies(dto.targetBabies())
                .targetAudiences(dto.targetAudiences())
                .analyzeInput(job.getAnalyzeInput())
                .analyzeResult(job.getAnalyzeResult())
                .build();
        Memo savedMemo = memoRepository.save(newMemo);
        return savedMemo.getMemoId();
    }


    private Memo findMemo(Long memoId) {
        return memoRepository.findById(memoId)
                .orElseThrow(() -> new DataNotFoundException("Memo Not Found"));
    }

}
