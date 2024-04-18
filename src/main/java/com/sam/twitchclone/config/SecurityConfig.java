package com.sam.twitchclone.config;

import com.sam.twitchclone.config.ExceptionHandler.CustomAccessDeniedExceptionHandler;
import com.sam.twitchclone.config.ExceptionHandler.CustomLogoutHandler;
import com.sam.twitchclone.service.UserDetailsServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(1)
public class SecurityConfig {

    private final UserDetailsServiceImp userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedExceptionHandler customAccessDeniedExceptionHandler;
    private final CustomLogoutHandler logoutHandler;


    @Bean
    CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow specific origins (replace with your React app's URL):
//        configuration.setAllowedOrigins(List.of("*"));// doesn't work
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // works for only :3000
//        configuration.setAllowedOrigins(List.of("http://localhost:3001")); // works for only :3001
//        configuration.setAllowedOriginPatterns(List.of("*")); // works for all origin

        // Allow WebSocket methods (crucial for connection establishment):
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"));

        // Include necessary headers:
        configuration.setAllowedHeaders(List.of("Authorization", "Origin", "Content-Type", "Upgrade"));

        // If using credentials (e.g., JWT tokens):
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(Customizer.withDefaults())
//                .authorizeHttpRequests(
//                        req -> req.requestMatchers("/api/v1/auth/**")
//                                .permitAll()
//                                .requestMatchers("/secured/**", "/secured/socket", "/secured/success")
//                                .authenticated()
//                                .anyRequest().authenticated()
//                );
////                .authorizeHttpRequests(authorize -> authorize
////                        .anyRequest().authenticated()
////                );
////                .oauth2Login(Customizer.withDefaults());
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
//                .cors(Customizer.withDefaults())
//                .cors(cors->cors.disable())
                .cors((cors) -> cors
                        .configurationSource(apiConfigurationSource())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
//                        req -> req.requestMatchers(
//                                        "/api/v1/auth/authenticate",
//                                        "/api/v1/auth/register"
//                                )
                        req -> req.requestMatchers(
                                        "/api/v1/auth/**",
                                        "/api/v1/onPublish",
                                        "/api/v1/onPublishDone",
                                        "/api/v1/user/recommendations",
                                        "/api/v1/user/username/**"
                                )
                                .permitAll()
                                .requestMatchers("/chat/**").permitAll()
//                                .requestMatchers("/app/**/**", "/app/**/**/**", "/app/socket", "/app/success").authenticated()
                                .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .userDetailsService(userDetailsService)
                .exceptionHandling(
                        e -> e.accessDeniedHandler(customAccessDeniedExceptionHandler)
                                .authenticationEntryPoint(customAccessDeniedExceptionHandler))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(l -> l.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(
                                (request, response, authentication) -> SecurityContextHolder.clearContext()
                        )
                )
//                .oauth2Login(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}

