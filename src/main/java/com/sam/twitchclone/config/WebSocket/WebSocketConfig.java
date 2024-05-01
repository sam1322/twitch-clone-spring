package com.sam.twitchclone.config.WebSocket;

import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.service.JwtService;
import com.sam.twitchclone.service.UserDetailsServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtService jwtService;
    private final UserDetailsServiceImp userDetailsService;
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry){
//        registry.addEndpoint("/ws","/wss","/app").withSockJS();
//
//    }
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry){
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.enableSimpleBroker("/topic");
//    }
//

    //    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/secured/history");
//        config.setApplicationDestinationPrefixes("/spring-security-mvc-socket");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/secured/chat")
//                .setAllowedOriginPatterns("*")
//                .withSockJS();
//    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat");
        registry.addEndpoint("/chat")
//                .setAllowedOrigins("*")
//                .setAllowedOrigins("http://localhost:3000")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }


//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        final CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowCredentials(false);
//        configuration.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "CONNECT", "OPTIONS", "TRACE", "PATCH"));
//        configuration.setAllowedHeaders(Arrays.asList("Accept","Content-Type", "Access-Control-Allow-Headers", "Origin",
//                "x-requested-with", "Authorization"));
//        configuration.setExposedHeaders(Arrays.asList("X-Auth-Token","Authorization"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                try {
                    log.info("Inside the websocket interceptor");
                    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                    if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                        String authHeader = accessor.getFirstNativeHeader("Authorization");

                        String token = authHeader.substring(7);

                        // TODO : add a try catch here and a logger to track whether the provided token is valid or invalid
                        log.info("Going to extract username from token");
                        String userId = jwtService.extractUsername(token); // if token is invalid it will give error and thus will not establish the STOMP connection


                        // here userId is email
//                        System.out.println("Security Context : " + SecurityContextHolder.getContext().getAuthentication());
                        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            log.info("Inside the iff statement");
                            User userDetails = userDetailsService.loadUserByUserId(UUID.fromString(userId));
                            System.out.println("Hello userdetails : " + userDetails.getUsername());
                            if (jwtService.isValid(token, userDetails)) {
                                System.out.println("jwt token valid ");
                                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );
                                SecurityContextHolder.getContext().setAuthentication(authToken);

                                return message;
                            }
                        }
                    }
                } catch (Exception error) {
                    log.error("Error in websocket interceptor : " + error.getMessage());
                    throw new IllegalArgumentException("Invalid User");
                }

                return message;
            }
        });
    }
//    @Override
//    public void configureClientOutboundChannel(ChannelRegistration registration) {
//        registration.taskExecutor().corePoolSize(4).maxPoolSize(8);
//        registration.setInterceptors (new WebSocketHandshakeHandlerInterceptor());
//    }
}
