package com.sam.twitchclone.service;

import com.sam.twitchclone.controller.auth.dto.AuthenticationRequest;
import com.sam.twitchclone.controller.auth.dto.AuthenticationResponse;
import com.sam.twitchclone.controller.auth.dto.RegisterRequest;
import com.sam.twitchclone.dao.postgres.model.Token;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.TokenRepository;
import com.sam.twitchclone.dao.postgres.repository.UserRepository;
import com.sam.twitchclone.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final TimeUtil timeUtil;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {

            return AuthenticationResponse.builder()
                    .message("User already exist")
                    .build();
        }

        Instant currentTime = timeUtil.getCurrentTime();
//        ZonedDateTime currentTime = timeUtil.getCurrentTime();

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


        saveUserToken(jwtToken, user, currentTime);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("User successfully created")
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
        log.info("generating token for login " + jwtToken);

        Instant currentTime = timeUtil.getCurrentTime();
        revokeAllTokensByUser(user, currentTime);

        saveUserToken(jwtToken, user, currentTime);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("User login successful")
                .build();
    }

    private void revokeAllTokensByUser(User user, Instant currentTime) {
        List<Token> validTokenListByUser = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validTokenListByUser.isEmpty()) {
            // The latest token is the first element in the sorted list
            Token latestToken = validTokenListByUser.get(0);

//            for (Token token : validTokenListByUser) {
            for (int i = 1; i < validTokenListByUser.size(); i++) {
                Token token = validTokenListByUser.get(i);
                token.setLoggedOut(true);
                token.setUpdatedTime(currentTime);
                System.out.println("token : " + token.getToken() + ", userId : " + token.getUser().getEmail() + ", createdTime : " + token.getCreatedTime());
            }
            tokenRepository.saveAll(validTokenListByUser);
        }
    }

    //    private void saveUserToken(String jwtToken, User user, ZonedDateTime currentTime) {
    private void saveUserToken(String jwtToken, User user, Instant currentTime) {
        Token token = Token.builder()
                .token(jwtToken)
                .createdTime(currentTime)
                .updatedTime(currentTime)
                .user(user)
                .build();
        log.info("saving token " + jwtToken);

        tokenRepository.save(token);
    }

}
