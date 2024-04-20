package com.sam.twitchclone.controller.block.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockResponse {
    private BlockerDTO srcBlocker;
    private BlockerDTO endBlocker;
}