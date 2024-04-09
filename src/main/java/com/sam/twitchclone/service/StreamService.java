package com.sam.twitchclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamService {
    private final SecurityService securityService;
    public String generateStreamKey(){
        String userId = securityService.getUserInfo().getUserId();
        return  userId;
    }
}
