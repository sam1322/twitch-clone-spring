package com.sam.twitchclone.controller.auth.dto;

import com.sam.twitchclone.model.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse  {
    private String token;
    private String message;
}
