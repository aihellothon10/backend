package com.example.memoservice.domain.chat.repository;

import com.example.memoservice.domain.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    //    Optional<ChatRoom> findByJobIds(Long jobId);
    @Query("SELECT c FROM ChatRoom c JOIN c.jobIds j WHERE j = :jobId")
    ChatRoom findByJobId(@Param("jobId") Long jobId);
}
