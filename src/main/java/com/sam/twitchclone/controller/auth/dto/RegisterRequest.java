package com.sam.twitchclone.controller.auth.dto;

import com.sam.twitchclone.dao.postgres.model.user.Role;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private Role role;
}
