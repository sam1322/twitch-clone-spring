package com.sam.twitchclone.controller.stream.dto;

import com.sam.twitchclone.constant.enums.VideoStatus;
import com.sam.twitchclone.controller.user.dto.UserDetail;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
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
public class VideoResponse {
    private UUID videoId;

    private Instant createdAt;
    private Instant updatedAt;


    private String title;
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VideoStatus videoStatus;

    private double videoSize;

    private String videoUrl;

    private String thumbnailUrl;

    private UserDetail user;

}
