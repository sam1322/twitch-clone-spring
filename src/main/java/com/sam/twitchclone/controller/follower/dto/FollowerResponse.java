package com.sam.twitchclone.controller.follower.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowerResponse {
    private FollowerDTO srcFollower;
    private FollowerDTO endFollower;
}
