package com.sam.twitchclone.controller.auth.dto;

import com.sam.twitchclone.constant.enums.SignUpEnum;
import com.sam.twitchclone.dao.postgres.model.user.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String code;
    @NonNull
    private SignUpEnum loginProvider;
    private String userName;
    private String userImage;
    @Deprecated
    private String firstName;
    @Deprecated
    private String lastName;
    private String email;
    private String password;
    @NonNull
    private Role role;
}
