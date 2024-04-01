package com.sam.twitchclone.config;

import com.sam.twitchclone.model.BaseResponse;
import com.sam.twitchclone.model.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<BaseResponse> handleGenericException(Exception exception) {
        log.error("Unhandled error", exception);
        BaseResponse body = BaseResponse.builder().error(true).errorCode(ErrorCode.INTERNAL_SERVER_ERROR).message("An unexpected error occurred").build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<BaseResponse> handleBadRequest(Exception exception) {
        log.error("Error:", exception);
        BaseResponse body = BaseResponse.builder().error(true).errorCode(ErrorCode.BAD_REQUEST).message(exception.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
