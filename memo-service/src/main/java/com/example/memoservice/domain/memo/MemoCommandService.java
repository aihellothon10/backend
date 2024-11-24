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
public class MemoCommandService {

    private final MemoRepository memoRepository;
    private final VectorStore vectorStore;


    public void favoriteMemo(Long memoId) {
        Memo memo = findMemo(memoId);
        memo.favorite();
        memoRepository.save(memo);
    }

    public void hiddenMemo(Long memoId) {
        Memo memo = findMemo(memoId);
        memo.hidden();
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
