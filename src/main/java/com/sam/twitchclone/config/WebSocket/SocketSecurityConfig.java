package com.sam.twitchclone.config.WebSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class SocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
//    @Override
//    protected void configureInbound(
//            MessageSecurityMetadataSourceRegistry messages) {
//        messages
////                .simpDestMatchers("/secured/**").authenticated()
//                .simpDestMatchers("/chat/**").authenticated()
//                .simpDestMatchers("/app/**").authenticated()
//                .anyMessage().authenticated();
//    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.anyMessage().permitAll();
    }
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

}