package com.sam.twitchclone.controller.stream.dto;

import com.sam.twitchclone.constant.enums.StreamStatus;
import com.sam.twitchclone.dao.postgres.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StreamResponse {
    private String streamKey;
    private Instant createdAt;
    private Instant updatedAt;
    private User user;
    private StreamStatus status;
}
