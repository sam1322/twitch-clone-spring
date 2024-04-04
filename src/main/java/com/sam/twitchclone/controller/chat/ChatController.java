package com.sam.twitchclone.controller.chat;

import com.sam.twitchclone.controller.chat.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")

    @MessageMapping("/chatMessage")   // user will send message through /app/chatMessage
    @SendTo("/topic/chat")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        return  chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ){
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/secured/chat")
    @SendTo("/secured/history")
    public ChatMessage sendMessage2(@Payload ChatMessage chatMessage){
        return  chatMessage;
    }


}
