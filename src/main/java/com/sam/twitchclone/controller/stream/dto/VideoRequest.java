package com.sam.twitchclone.controller.stream.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoRequest {
    private String title;
    private String thumbnailUrl;
}
