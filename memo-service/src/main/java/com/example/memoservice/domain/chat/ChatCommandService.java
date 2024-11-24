package com.example.memoservice.domain.chat;

import com.example.memoservice.domain.analizer.AnalyzeCommandService;
import com.example.memoservice.domain.analizer.AnalyzeRequest;
import com.example.memoservice.domain.analizer.JobQueryService;
import com.example.memoservice.domain.analizer.model.*;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatRequest;
import com.example.memoservice.domain.chat.model.ChatRoom;
import com.example.memoservice.domain.chat.model.HelpVMessage;
import com.example.memoservice.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.memoservice.domain.analizer.service.helpyv.HelpyVSystemPrompt.BASE_PROMPT;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final AnalyzeCommandService analyzeCommandService;
    private final JobQueryService jobQueryService;


    public Long createChatRoom() {
        ChatRoom chatRoom = new ChatRoom();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // 시스템 프롬프트 초기화
        var systemPromptContents = BASE_PROMPT.stream()
                .map(HelpVMessage.Content::ofText)
                .toList();
        HelpVMessage systemMessage = HelpVMessage.ofSystem(systemPromptContents);
        chatRoom.addMessage(systemMessage);

        return savedChatRoom.getChatRoomId();
    }

    public List<HelpVMessage> getMessages(Long chatRoomId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        return chatRoom.getMessages();
    }


    // 일단 이미지 무시
    public Long requestText(Long chatRoomId,
                            String text,
                            List<String> links) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);

        // 메시지는 별도로 저장해 놓는다...
        var userContent = HelpVMessage.Content.ofText(text);
        HelpVMessage userMessage = HelpVMessage.ofUser(List.of(userContent));
        chatRoom.addMessage(userMessage);

        List<HelpyVChatRequest.MessageDto> messages = chatRoom.getMessages().stream()
                .map(m -> new HelpyVChatRequest.MessageDto(
                        m.getRole(),
                        m.getContent().stream()
                                .map(HelpVMessage.Content::toRequestContent)
                                .toList()
                ))
                .toList();

        HelpyVChatRequest request = new HelpyVChatRequest(
                "helpy-v-large",
                messages
        );


        Long jobId = analyzeCommandService.requestAnalyzeForChat(
                new AnalyzeRequest(text, JobType.Memo, links), request, null // 이미지 생략
        );

        chatRoom.addJob(jobId);

        return jobId;
    }

    public Long requestAnalyze(Long chatRoomId,
                               AnalyzeRequest requestForm,
                               List<MultipartFile> images) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);

        // 메시지는 별도로 저장해 놓는다...
        var userContent = HelpVMessage.Content.ofText(requestForm.getQuery());
        HelpVMessage userMessage = HelpVMessage.ofUser(List.of(userContent));
        chatRoom.addMessage(userMessage);

        List<HelpyVChatRequest.MessageDto> messages = chatRoom.getMessages().stream()
                .map(m -> new HelpyVChatRequest.MessageDto(
                        m.getRole(),
                        m.getContent().stream()
                                .map(HelpVMessage.Content::toRequestContent)
                                .toList()
                ))
                .toList();

        HelpyVChatRequest request = new HelpyVChatRequest(
                "helpy-v-large",
                messages
        );


        Long jobId = analyzeCommandService.requestAnalyzeForChat(
                new AnalyzeRequest(requestForm.getQuery(), JobType.Memo, requestForm.getLinks()),
                request,
                images
        );

        chatRoom.addJob(jobId);

        return jobId;
    }

    public List<JobResponse> getJobs(Long chatRoomId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);

        return chatRoom.getJobIds().stream()
                .map(jobQueryService::getJob)
                .map(JobResponse::of)
                .toList();
    }

    // 채팅에 대한 정보도 포함
    public record JobResponse(Long jobId,
                              String q,
                              String a,
                              AnalysisStatus status,
                              AnalyzeInput analyzeInput,
                              AnalyzeResult analyzeResult) {
        public static JobResponse of(Job job) {
            return new JobResponse(
                    job.getJobId(),
                    job.getAnalyzeInput().getQuery(),
                    job.getAnalyzeResult().getAnswer(),
                    job.getStatus(),
                    job.getAnalyzeInput(),
                    job.getAnalyzeResult()
            );
        }
    }


    // =================== TEXT ===========================
    public void addUserMessage(Long chatRoomId, String text) {
        var userContent = HelpVMessage.Content.ofText(text);
        HelpVMessage userMessage = HelpVMessage.ofUser(List.of(userContent));

        ChatRoom chatRoom = getChatRoom(chatRoomId);
        chatRoom.addMessage(userMessage);
    }

    public void addUserImage(Long chatRoomId, String imageUrl) {
        var userContent = HelpVMessage.Content.ofImage(imageUrl);
        HelpVMessage userMessage = HelpVMessage.ofUser(List.of(userContent));

        ChatRoom chatRoom = getChatRoom(chatRoomId);
        chatRoom.addMessage(userMessage);
    }
    // ====================================================

    public void addAssistantMessage(Long jobId) {
        ChatRoom chatRoom = chatRoomRepository.findByJobId(jobId);
        if (chatRoom == null) {
            return;
        }

        Job job = jobQueryService.getJob(jobId);

        var assistantContent = HelpVMessage.Content.ofText(job.getAnalyzeResult().getAssistantContent());
        HelpVMessage assistantMessage = HelpVMessage.ofAssistant(List.of(assistantContent));
        chatRoom.addMessage(assistantMessage);
    }


    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found:" + chatRoomId));
    }

}
