package com.example.memoservice.domain.chat.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @ElementCollection
    private List<HelpVMessage> messages = new ArrayList<>();

    //    @OneToMany(mappedBy = "chatRoom")
//    @OrderBy("jobId asc")
//    private List<Job> jobs = new ArrayList<>();
    @ElementCollection
    private List<Long> jobIds = new ArrayList<>();

    public void addJob(Long jobId) {
        jobIds.add(jobId);
    }

    public void addMessage(HelpVMessage message) {
        messages.add(message);
    }

}
