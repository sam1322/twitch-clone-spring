package com.sam.twitchclone.controller.chat;

import com.sam.twitchclone.controller.chat.dto.ChatMessage;
import com.sam.twitchclone.controller.chat.dto.ChatResponse;
import com.sam.twitchclone.dao.postgres.Chat;
import com.sam.twitchclone.dao.postgres.model.Video;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.VideoRepository;
import com.sam.twitchclone.service.ChatService;
import com.sam.twitchclone.service.JwtService;
import com.sam.twitchclone.service.SecurityService;
import com.sam.twitchclone.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final SecurityService securityService;
    private final UserService userService;
    private final JwtService jwtService;
    private final ChatService chatService;
    private final VideoRepository videoRepository;


//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")

    //    @MessageMapping("/chatMessage")   // user will send message through /app/chatMessage
//    @SendTo("/topic/chat")
    @MessageMapping("/chatMessage/{videoId}")
//    @SendToUser("/topic/chat/{videoId}")
    @SendTo("/topic/chat/{videoId}")

    public ChatResponse sendMessage(@Payload ChatMessage chatMessage, @Header("Authorization") String authorizationHeader, @DestinationVariable String videoId) {
//        long currentTime = System.currentTimeMillis();
//        String userId = securityService.getUserInfo().getUserId();

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof User) {
//            User user = (User) authentication.getPrincipal();
//            userId = user.getId().toString();
//            chatMessage.setUserId(userId);
//        }
        log.info("Headers:" + authorizationHeader);

        String token = authorizationHeader.substring(7);
        String userId = jwtService.extractUsername(token); // if to

//        String videoId = chatMessage.getVideoId();
        System.out.println("userId: " + userId);
        System.out.println("videoId: " + videoId);
        User user = userService.getUser(UUID.fromString(userId));
        Optional<Video> video = videoRepository.findById(UUID.fromString(videoId));
        if (video.isEmpty()) {
            throw new IllegalArgumentException("Video not found");
        }
//        chatMessage.setName(user.getFullName());
//        chatMessage.setTimeStamp(String.valueOf(currentTime));
        Instant currentTime = Instant.now();
        Chat chat = Chat.builder()
                .message(chatMessage.getMessage())
                .user(user)
                .video(video.get())
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .build();
        return chatService.saveChat(chat);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
//        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        long currentTime = System.currentTimeMillis();
//        String userId= chatMessage.getUserId();
        String userId = securityService.getUserInfo().getUserId();
//        String videoId = chatMessage.getVideoId();
//        System.out.println("userId: " + userId);
//        System.out.println("videoId: " + videoId);
//        chatMessage.setTimeStamp(String.valueOf(currentTime));
        return chatMessage;
    }

    @MessageMapping("/secured/chat")
    @SendTo("/secured/history")
    public ChatMessage sendMessage2(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }


}
