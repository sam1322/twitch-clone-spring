package com.sam.twitchclone.controller.user.dto;

import com.sam.twitchclone.controller.stream.dto.StreamResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private UUID userId;
    private String email;
    private boolean verified;
    private String userName;
    private String userImage;
    private String bio;
    //    private Role role;
    private StreamResponse currentStream;
}
