package com.sam.twitchclone.controller.user.dto;

import com.sam.twitchclone.dao.postgres.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetail {
    private UUID userId;
    private String email;
    private Boolean isVerified;
    private String userName;
    private Role role;
}
