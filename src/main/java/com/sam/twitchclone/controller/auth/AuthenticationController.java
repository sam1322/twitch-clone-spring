package com.sam.twitchclone.controller.auth;

import com.sam.twitchclone.controller.auth.dto.AuthenticationRequest;
import com.sam.twitchclone.controller.auth.dto.AuthenticationResponse;
import com.sam.twitchclone.controller.auth.dto.RegisterRequest;
import com.sam.twitchclone.model.BaseResponse;
import com.sam.twitchclone.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws IOException {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

//    @GetMapping("/code")
//    public ResponseEntity<AuthenticationResponse> authenicateCode(@RequestBody AuthenticationRequest request) throws IOException {
////        authenticationService.authenticateCode(request.getCode());
////        return ResponseEntity.ok(BaseResponse.builder()
////                .message("Hello")
////                .build());
//        return ResponseEntity.ok().body(authenticationService.authenticateCode(request.getCode()));
//    }

    @GetMapping("/idToken")
    public ResponseEntity<BaseResponse> authIdToken(@RequestBody AuthenticationRequest request) throws IOException {

        authenticationService.VerifyIdToken(request.getIdToken());
        return ResponseEntity.ok(BaseResponse.builder()
                .message("Hello")
                .build());
    }

}
