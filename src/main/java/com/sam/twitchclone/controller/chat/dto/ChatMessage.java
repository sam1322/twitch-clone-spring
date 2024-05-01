package com.sam.twitchclone.controller.chat.dto;

import com.sam.twitchclone.constant.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;

//    private String userId;
//    private String videoId;
//    private String name;
//    private String timeStamp;

    private String message;

}
