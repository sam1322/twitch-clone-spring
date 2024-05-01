package com.sam.twitchclone.mapper;

import com.sam.twitchclone.controller.chat.dto.ChatResponse;
import com.sam.twitchclone.dao.postgres.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface ChatMapper {
    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "name", source = "chat.user.fullName")
    ChatResponse chatToChatResponse(Chat chat);

    List<ChatResponse> chatListToChatResponseList(List<Chat> chat);
}
