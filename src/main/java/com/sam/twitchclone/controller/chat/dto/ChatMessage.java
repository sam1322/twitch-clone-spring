package com.sam.twitchclone.controller.chat.dto;

import com.sam.twitchclone.constant.enums.MessageType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;
    private String message;
}
