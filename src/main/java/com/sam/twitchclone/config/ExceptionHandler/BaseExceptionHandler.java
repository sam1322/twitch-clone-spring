package com.sam.twitchclone.config.ExceptionHandler;

import com.sam.twitchclone.model.BaseResponse;
import com.sam.twitchclone.model.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<BaseResponse> handleGenericException(Exception exception) {
        log.error("Unhandled error", exception);
        BaseResponse body = BaseResponse.builder().error(true).errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
//                .message("An unexpected error occurred").build();
                .message(exception.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<BaseResponse> handleBadRequest(Exception exception) {
        log.error("Error:", exception);
        BaseResponse body = BaseResponse.builder().error(true).errorCode(ErrorCode.BAD_REQUEST).message(exception.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<BaseResponse> handleAuthenticationException(Exception exception) {

        log.error("Authentication Exception Error:", exception);
        BaseResponse body = BaseResponse.builder().error(true).errorCode(ErrorCode.UNAUTHORIZED).message(exception.getMessage()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);

    }

//    @ExceptionHandler(value = {AccessDeniedException.class, AccessDeniedCustomException.class})
    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<BaseResponse> handleAccessDenied(Exception exception) {
        log.error("Access Denied Error:", exception);
        BaseResponse body = BaseResponse.builder().error(true).errorCode(ErrorCode.ACCESS_DENIED).message(exception.getMessage()).build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<BaseResponse> handleNotFound(NoResourceFoundException exception) {
        log.error("Error:", exception);
        BaseResponse body = BaseResponse.builder().error(true).errorCode(ErrorCode.NOT_FOUND).message(exception.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

}
