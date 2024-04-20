package com.sam.twitchclone.controller.block.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockerDTO {
    private UUID blockerId;
    private String fullName;
    private String email;
    private String userImage;
}
