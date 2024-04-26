package com.sam.twitchclone.controller.stream.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StreamRequest {
    private boolean live;
    private boolean chatEnabled;
    private boolean chatDelayed;
    private boolean chatFollowerOnly;
}
