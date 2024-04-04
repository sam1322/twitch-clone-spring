package com.sam.twitchclone.controller.auth.dto;

import com.sam.twitchclone.constant.enums.SignUpEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @NotNull
    @Valid
    private SignUpEnum loginProvider;
    private String email;
    private String password;
    private String code ;
    private String idToken;
}
