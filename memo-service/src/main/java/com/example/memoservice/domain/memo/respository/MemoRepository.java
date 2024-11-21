package com.example.memoservice.domain.memo.respository;

import com.example.memoservice.domain.memo.model.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {
}
