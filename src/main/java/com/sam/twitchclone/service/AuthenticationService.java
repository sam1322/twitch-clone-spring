package com.sam.twitchclone.service;

import com.sam.twitchclone.controller.auth.dto.AuthenticationRequest;
import com.sam.twitchclone.controller.auth.dto.AuthenticationResponse;
import com.sam.twitchclone.controller.auth.dto.RegisterRequest;
import com.sam.twitchclone.dao.postgres.model.user.Role;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {

            return AuthenticationResponse.builder()
                    .message("User already exist")
                    .build();
        }
        Instant currentTime = Instant.now();
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .createdTime(currentTime)
                .updatedTime(currentTime)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.USER)
                .role(request.getRole())
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("User login successful")
                .build();
    }
}
