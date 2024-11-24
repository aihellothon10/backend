package com.example.memoservice.domain.chat.representation;

import com.example.memoservice.domain.analizer.AnalyzeRequest;
import com.example.memoservice.domain.analizer.AnalyzeRequestForm;
import com.example.memoservice.domain.chat.ChatCommandService;
import com.example.memoservice.domain.chat.ChatQueryService;
import com.example.memoservice.domain.chat.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatRestController {

    private final ChatCommandService chatCommandService;
    private final ChatQueryService chatQueryService;

    @PostMapping
    ChatRoomIdResponse createRoom() {
        return new ChatRoomIdResponse(chatCommandService.createChatRoom());
    }

    @GetMapping("/{chatRoomId}")
    ChatRoom getChatRoom(@PathVariable Long chatRoomId) {
        return chatQueryService.getChatRoom(chatRoomId);
    }

    @PostMapping("/{chatRoomId}/analyze")
    JobIdResponse requestAnalyze(@PathVariable Long chatRoomId,
                                 @RequestBody ChatAnalyzeRequest request) {
        return new JobIdResponse(chatCommandService.requestText(chatRoomId, request.text, request.links));
    }

    @PostMapping(value = "/{chatRoomId}/analyze/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JobIdResponse requestAnalyzeTest(@PathVariable Long chatRoomId,
                                     @RequestPart AnalyzeRequest request,
                                     @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return new JobIdResponse(chatCommandService.requestAnalyze(chatRoomId, request, images));
    }

    @PostMapping(value = "/{chatRoomId}/analyze/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JobIdResponse requestAnalyzeForm(@PathVariable Long chatRoomId,
                                     @ModelAttribute AnalyzeRequestForm request) {
        return new JobIdResponse(chatCommandService.requestAnalyze(
                chatRoomId,
                new AnalyzeRequest(
                        request.getQuery(),
                        request.getJobType(),
                        request.getLinks()),
                request.getImages())
        );
    }

    @GetMapping("/{chatRoomId}/jobs")
    List<ChatCommandService.JobResponse> getJobx(@PathVariable Long chatRoomId) {
        return chatCommandService.getJobs(chatRoomId);
    }


//    @GetMapping("/{chatRoomId}/messages")
//    List<HelpVMessage> getChatRoomMessages(@PathVariable Long chatRoomId) {
//        return chatCommandService.getMessages(chatRoomId);
//    }
//
//    @PostMapping("/{chatRoomId}/chat")
//    void addUserMessage(@PathVariable Long chatRoomId,
//                        @RequestBody ChatTextRequest request) {
//        chatCommandService.addUserMessage(chatRoomId, request.text);
//    }
//
//    @PostMapping("/{chatRoomId}/image")
//    void addUserImage(@PathVariable Long chatRoomId,
//                      @RequestBody ChatImageRequest request) {
//        chatCommandService.addUserImage(chatRoomId, request.url);
//    }

    record ChatRoomIdResponse(Long chatRoomId) {
    }

    record ChatTextRequest(String text) {
    }

    record ChatAnalyzeRequest(String text,
                              List<String> links) {
    }

    record ChatImageRequest(String url) {
    }


    record JobIdResponse(Long jobId) {
    }

}
