package com.sam.twitchclone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedExceptionHandler extends ResponseEntityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized", authException.getMessage());

    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        sendErrorResponse(response, HttpStatus.FORBIDDEN, "Access Denied", accessDeniedException.getMessage());
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String error, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");

        Map<String, String> data = new HashMap<>();
        data.put("error", error);
        data.put("message", message);

        OutputStream outputStream = response.getOutputStream();
        objectMapper.writeValue(outputStream, data);
        outputStream.flush();

//        ResponseEntity<Map<String, String>> responseEntity = ResponseEntity
//                .status(status)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(data);
//
//        responseEntity.writeTo(response.getOutputStream());

    }
}
