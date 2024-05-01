package com.sam.twitchclone.controller.chat;

import com.sam.twitchclone.controller.chat.dto.ChatResponse;
import com.sam.twitchclone.model.BaseResponse;
import com.sam.twitchclone.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
@Slf4j
@RequiredArgsConstructor
public class ChatHistoryController {
    private final ChatService chatService;

    @GetMapping("/v1/chat/history/{videoId}")
    public ResponseEntity<List<ChatResponse>> getChatHistory(@PathVariable UUID videoId) {  // ie get list of followers for the provided userId ie list of followers whose end follower is userId

        return ResponseEntity.ok(chatService.getChatHistoryList(videoId));
    }

    @DeleteMapping("/v1/chat/delete/{videoId}")
    public ResponseEntity<BaseResponse> deleteChatHistory(@PathVariable UUID videoId) {  // ie get list of followers for the provided userId ie list of followers whose end follower is userId
        chatService.deleteChatHistory(videoId);
        return ResponseEntity.ok(BaseResponse.builder().message("Chat history deleted successfully").build());
    }
}
