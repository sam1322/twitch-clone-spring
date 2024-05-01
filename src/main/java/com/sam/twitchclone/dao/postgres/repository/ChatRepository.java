package com.sam.twitchclone.dao.postgres.repository;

import com.sam.twitchclone.dao.postgres.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findChatByVideoId(UUID videoId);
}
