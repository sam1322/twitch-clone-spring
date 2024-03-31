package com.sam.twitchclone.config;

import com.sam.twitchclone.service.UserDetailsServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImp userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomExceptionHandler customExceptionHandler;
    private final CustomLogoutHandler logoutHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
//                        req->req.requestMatchers("/login/**","/register/**")
                        req -> req.requestMatchers(
                                        "/api/v1/auth/authenticate",
                                        "/api/v1/auth/register"
                                )
//                        req -> req.requestMatchers("/api/v1/auth/**")
                                .permitAll()
                                .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .userDetailsService(userDetailsService)
                .exceptionHandling(
                        e -> e.accessDeniedHandler(customExceptionHandler)
                                .authenticationEntryPoint(customExceptionHandler))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(l -> l.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(
                                (request, response, authentication) -> SecurityContextHolder.clearContext()
                        )
                )
//                .exceptionHandling(
//                        e -> e.accessDeniedHandler(customAccessDeniedHandler)
//                                .authenticationEntryPoint(customAuthenticationEntryPoint))
//
//                        e -> e.accessDeniedHandler(
//                                        (request, response, accessDeniedException)->response.setStatus(403)
//                                )
//                           .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

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
