package com.sam.twitchclone.controller.stream.dto;

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
//    private User user;
    //    private StreamStatus status;
    private boolean live;
    private boolean expired;
    private boolean chatEnabled;
    private boolean chatDelayed;
    private boolean chatFollowerOnly;
    private VideoResponse currentVideo;
}
