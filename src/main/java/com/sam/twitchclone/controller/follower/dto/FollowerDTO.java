package com.sam.twitchclone.controller.follower.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowerDTO {
    private UUID followerId;
    private String fullName;
    private String email;
    private String userImage;
}
