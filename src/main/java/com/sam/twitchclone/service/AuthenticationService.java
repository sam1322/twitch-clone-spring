package com.sam.twitchclone.service;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.sam.twitchclone.constant.enums.SignUpEnum;
import com.sam.twitchclone.controller.auth.dto.AuthenticationRequest;
import com.sam.twitchclone.controller.auth.dto.AuthenticationResponse;
import com.sam.twitchclone.controller.auth.dto.RegisterRequest;
import com.sam.twitchclone.dao.postgres.model.Token;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.TokenRepository;
import com.sam.twitchclone.dao.postgres.repository.UserRepository;
import com.sam.twitchclone.util.CryptoUtil;
import com.sam.twitchclone.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
    private final CryptoUtil cryptoUtil;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    public AuthenticationResponse register(RegisterRequest request) throws IOException {
//        if(request.getLoginProvider()){
//            throw new IllegalArgumentException("loginProvider cannot be null");
//        }
        SignUpEnum loginProvider = request.getLoginProvider();
        String email = request.getEmail();
//        String firstName = request.getFirstName();
//        String lastName = request.getLastName();

        String userName = request.getUserName();
        String userImage = request.getUserImage();
        String password = request.getPassword();

        if (loginProvider.equals(SignUpEnum.GOOGLE)) {
            String id_token = getIdTokenFromCode(request.getCode());
            GoogleIdToken idToken = VerifyIdToken(id_token);
            GoogleIdToken.Payload payload = idToken.getPayload();
            if (!payload.getEmailVerified()) {
                throw new IllegalArgumentException("Email not verified");
            }
            email = payload.getEmail();
            userName = (String) payload.getUnknownKeys().getOrDefault("name", "User");
            userImage = (String) payload.getUnknownKeys().getOrDefault("picture", "");
            password = UUID.randomUUID().toString().substring(0, 11);
//            password = cryptoUtil.encodePassword(UUID.randomUUID().toString().substring(0, 11));

        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User already exist");
        }

        Instant currentTime = timeUtil.getCurrentTime();

        User user = User.builder()
                .userName(userName)
                .userImage(userImage)
                .createdTime(currentTime)
                .updatedTime(currentTime)
                .email(email)
                .password(passwordEncoder.encode(password))
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


    public AuthenticationResponse authenticate(AuthenticationRequest request) throws IOException {
        String email = request.getEmail();

        if (request.getLoginProvider().equals(SignUpEnum.GOOGLE)) {
            String id_token = getIdTokenFromCode(request.getCode());
            GoogleIdToken idToken = VerifyIdToken(id_token);
            GoogleIdToken.Payload payload = idToken.getPayload();
            if (!payload.getEmailVerified()) {
                throw new IllegalArgumentException("Email not verified");
            }
            email = payload.getEmail();
        } else {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }


        User user = userRepository.findByEmail(email)
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

    private String getIdTokenFromCode(String code) throws IOException {
        String message = "";
        String idToken = "";
        try {
            GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(), new GsonFactory(),
                    googleClientId, googleClientSecret,
                    code,
                    "http://localhost:3000")
                    .execute();

            return response.getIdToken();
//            System.out.println("Access token: " + response.getAccessToken());
//            System.out.println("ID token : " + response.getIdToken());


//            GoogleIdToken idToken = VerifyIdToken(response.getIdToken());
//            GoogleIdToken.Payload payload = idToken.getPayload();
//            System.out.println("email : " + payload.getEmail());
//            System.out.println("isEmail verified:" + payload.getEmailVerified());


        } catch (TokenResponseException e) {
            if (e.getDetails() != null) {
                System.err.println("Error: " + e.getDetails().getError());
                message = e.getDetails().getError();
                if (e.getDetails().getErrorDescription() != null) {
                    System.err.println(e.getDetails().getErrorDescription());
                    message = e.getDetails().getErrorDescription();
                }
                if (e.getDetails().getErrorUri() != null) {
                    System.err.println(e.getDetails().getErrorUri());
                    message = e.getDetails().getErrorUri();
                }
            } else {
                System.err.println(e.getMessage());
                message = e.getMessage();
            }
        }
        return idToken;
    }


//    public AuthenticationResponse authenticateCode(String code) throws IOException {
//        String message = "Login successful";
//        String jwtToken = null;
//        try {
//            GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(
//                    new NetHttpTransport(), new GsonFactory(),
//                    googleClientId, googleClientSecret,
//                    code,
//                    "http://localhost:3000")
//                    .execute();
////            System.out.println("Access token: " + response.getAccessToken());
////            System.out.println("ID token : " + response.getIdToken());
//
//
//            GoogleIdToken idToken = VerifyIdToken(response.getIdToken());
//            GoogleIdToken.Payload payload = idToken.getPayload();
//            System.out.println("email : " + payload.getEmail());
//            System.out.println("isEmail verified:" + payload.getEmailVerified());
//
//
//        } catch (TokenResponseException e) {
//            if (e.getDetails() != null) {
//                System.err.println("Error: " + e.getDetails().getError());
//                message = e.getDetails().getError();
//                if (e.getDetails().getErrorDescription() != null) {
//                    System.err.println(e.getDetails().getErrorDescription());
//                    message = e.getDetails().getErrorDescription();
//                }
//                if (e.getDetails().getErrorUri() != null) {
//                    System.err.println(e.getDetails().getErrorUri());
//                    message = e.getDetails().getErrorUri();
//                }
//            } else {
//                System.err.println(e.getMessage());
//                message = e.getMessage();
//            }
//        }
//        return AuthenticationResponse.builder()
//                .message(message)
//                .token(jwtToken)
//                .build();
//    }

    public GoogleIdToken VerifyIdToken(String id_token) {
        GoogleIdToken idToken;
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                new GsonFactory()).setAudience(Collections.singletonList(googleClientId))
                .build();
        try {
            idToken = verifier.verify(id_token);
            GoogleIdToken.Payload payload = idToken.getPayload();
            System.out.println("email : " + payload.getEmail());
            System.out.println("isEmail verified:" + payload.getEmailVerified());
//                if (!payload.getEmail().equalsIgnoreCase(loginRequest.getEmail())) {
//                    throw new IllegalArgumentException("email is not correct");
//                }
        } catch (Exception e) {
            throw new IllegalArgumentException("Id token invalid");
        }
        return idToken;
    }

    private void revokeAllTokensByUser(User user, Instant currentTime) {
        List<Token> validTokenListByUser = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validTokenListByUser.isEmpty() && validTokenListByUser.size() > 1) {
            // The latest token is the first element in the sorted list
            Token latestToken = validTokenListByUser.get(0);

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
