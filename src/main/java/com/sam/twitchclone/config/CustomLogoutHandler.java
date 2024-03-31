package com.sam.twitchclone.config;

import com.sam.twitchclone.dao.postgres.model.Token;
import com.sam.twitchclone.dao.postgres.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private  final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);
        // get stored token from database
        Token storedToken =  tokenRepository.findByToken(token).orElse(null);

        //invalidate the token ie. make logout true
        if(token!=null){
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
        }
        //save the token
    }
}
