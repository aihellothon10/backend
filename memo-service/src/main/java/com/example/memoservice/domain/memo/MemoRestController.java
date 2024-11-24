package com.example.memoservice.domain.memo;

import com.example.memoservice.domain.memo.model.MemoStatus;
import com.example.memoservice.domain.memo.respository.MemoDao;
import com.example.memoservice.domain.memo.respository.MemoListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memos")
class MemoRestController {

    private final MemoCommandService memoCommandService;
    private final MemoDocumentService memoDocumentService;
    private final MemoQueryService memoQueryService;

    private final MemoDao memoDao;

    @PostMapping
    MemoIdResponse createMemo(@RequestBody MemoCreateRequest request) {
        return new MemoIdResponse(memoDocumentService.createMemo(request));
    }

//    @GetMapping
//    List<Memo> getMemos() {
//        return memoQueryService.findAllMemos();
//    }


    @GetMapping
    List<MemoListDto> getMemoListByFilter(@RequestParam(required = false) String targetBabies,
                                          @RequestParam(required = false) String targetMembers,
                                          @RequestParam(required = false) String targetAudiences,
                                          @RequestParam(required = false) MemoStatus memoStatus) {
        return memoDao.getMemoListByFilter(targetBabies, targetMembers, targetAudiences, memoStatus);
    }

    @GetMapping("/{memoId}")
    MemoDetail getMemo(@PathVariable Long memoId) {
        return memoQueryService.getMemoWithComments(memoId);
    }


    @PutMapping("/{memoId}/favorite")
    void favorite(@PathVariable Long memoId) {
        memoCommandService.favoriteMemo(memoId);
    }

    @PutMapping("/{memoId}/hide")
    void hideMemo(@PathVariable Long memoId) {
        memoCommandService.hiddenMemo(memoId);
    }


    record MemoIdResponse(Long memoId) {
    }
}
