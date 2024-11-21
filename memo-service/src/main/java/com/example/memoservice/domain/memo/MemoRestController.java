package com.example.memoservice.domain.memo;

import com.example.memoservice.domain.memo.model.Memo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memos")
class MemoRestController {

    private final MemoDocumentService memoDocumentService;
    private final MemoQueryService memoQueryService;

    @PostMapping
    MemoIdResponse createMemo(@RequestBody MemoCreateRequest request) {
        return new MemoIdResponse(memoDocumentService.createMemo(request));
    }

    @GetMapping
    List<Memo> getMemos() {
        return memoQueryService.findAllMemos();
    }

    @GetMapping("/{memoId}")
    MemoDetail getMemo(@PathVariable Long memoId) {
        return memoQueryService.getMemoWithComments(memoId);
    }


    record MemoIdResponse(Long memoId) {
    }
}
