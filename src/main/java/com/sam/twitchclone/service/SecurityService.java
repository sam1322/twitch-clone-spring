package com.sam.twitchclone.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

//@Slf4j
@Service
public class SecurityService {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    public UserInfo getUserInfo() {
        if (authenticationFacade.getAuthentication() == null) {
            throw new IllegalStateException("User info requested but no security context available");
        }
        String subject = String.valueOf(authenticationFacade.getAuthentication().getName());
        try {
            UUID uuid = UUID.fromString(subject);
            //do something
        } catch (IllegalArgumentException exception) {
            //handle the case where string is not valid UUID
            subject = null;
        }
        return UserInfo.builder().userId(subject).build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String userId;
    }

    public interface IAuthenticationFacade {
        Authentication getAuthentication();
//        User getAuthentication();
//        UserDetails getAuthentication();
    }

    @Component
    public static class AuthenticationFacade implements IAuthenticationFacade {

        @Override
        public Authentication getAuthentication() {
            return SecurityContextHolder.getContext().getAuthentication();
        }
//
//                @Override
//        public User getAuthentication() {
//            return (User) SecurityContextHolder.getContext().getAuthentication();
//        }

//        @Override
//        public UserDetails getAuthentication() {
//            return (UserDetails) SecurityContextHolder.getContext().getAuthentication();
//        }

    }

}
