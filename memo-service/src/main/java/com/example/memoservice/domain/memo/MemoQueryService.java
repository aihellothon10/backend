package com.example.memoservice.domain.memo;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.comment.CommentQueryService;
import com.example.memoservice.domain.comment.model.Comment;
import com.example.memoservice.domain.memo.model.Memo;
import com.example.memoservice.domain.memo.respository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoQueryService {

    private final MemoRepository memoRepository;
    private final CommentQueryService commentQueryService;


    public Memo findMemo(Long memoId) {
        return memoRepository.findById(memoId)
                .orElseThrow(() -> new DataNotFoundException("Memo Not Found"));
    }

    public MemoDetail getMemoWithComments(Long memoId) {
        Page<Comment> comments = commentQueryService.getCommentsByMemoId(memoId, 0, 5);
        Memo memo = findMemo(memoId);
        return MemoDetail.of(memo, comments);
    }

    public List<Memo> findAllMemos() {
        return memoRepository.findAll();
    }

    public boolean existsMemo(Long memoId) {
        return memoRepository.existsById(memoId);
    }


}
