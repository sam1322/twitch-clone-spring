package com.sam.twitchclone.controller.stream;

import com.sam.twitchclone.model.BaseResponse;
import com.sam.twitchclone.service.StreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class StreamController {
    private final StreamService streamService;
    @PostMapping("/generate-key")
    public ResponseEntity<BaseResponse> generateStreamKey(){

        String streamKey = streamService.generateStreamKey();
        return ResponseEntity.ok(BaseResponse.builder()
                        .message(streamKey)
                .build());
    }
}
