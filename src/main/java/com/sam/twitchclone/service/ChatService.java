package com.sam.twitchclone.service;

import com.sam.twitchclone.controller.chat.dto.ChatResponse;
import com.sam.twitchclone.dao.postgres.Chat;
import com.sam.twitchclone.dao.postgres.repository.ChatRepository;
import com.sam.twitchclone.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    public List<ChatResponse> getChatHistoryList(UUID videoId) {
        List<Chat> chatList = chatRepository.findChatByVideoId(videoId);
        if (chatList != null) {
            return chatMapper.chatListToChatResponseList(chatList);
        }
        return null;
    }

    public void deleteChatHistory(UUID videoId) {
        chatRepository.deleteAll(chatRepository.findChatByVideoId(videoId));
    }

    public ChatResponse saveChat(Chat chat) {
        Chat chatMessage = chatRepository.save(chat);
        ChatResponse chatResponse = chatMapper.chatToChatResponse(chatMessage);
        return chatResponse;
    }
}
