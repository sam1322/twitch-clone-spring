package com.sam.twitchclone.controller.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatResponse {
    private UUID chatId;
    private String name;
    private String message;
    private Instant createdAt;
    private Instant updatedAt;
}
