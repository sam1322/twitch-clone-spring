package com.sam.twitchclone.controller.stream;

import com.sam.twitchclone.controller.stream.dto.StreamResponse;
import com.sam.twitchclone.dao.postgres.model.Video;
import com.sam.twitchclone.model.BaseResponse;
import com.sam.twitchclone.service.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Slf4j
public class StreamController {
    private final StreamService streamService;
    @Value("${rtmp.server.host}")
    private String rtmpServerHost;

    @PostMapping("/v1/generate-key")
    public ResponseEntity<BaseResponse> generateStreamKey() {

        String streamKey = streamService.generateStreamKey();
        return ResponseEntity.ok(BaseResponse.builder()
                .message(streamKey)
                .build());
    }

    @GetMapping("/v1/streamKey")
    public ResponseEntity<BaseResponse> getCurrentStreamKey() {

        String streamKey = streamService.getStreamKey();
        return ResponseEntity.ok(BaseResponse.builder()
                .message(streamKey)
                .build());
    }

    @PostMapping("/v1/validateStreamkey")
    public ResponseEntity<StreamResponse> validateStreamKey(@RequestParam String streamKey) {
        StreamResponse streamResponse = streamService.verifyKey(streamKey);
        return ResponseEntity.ok(streamResponse);
    }

    @PostMapping("/v1/onPublish")
    public ResponseEntity<Void> onPublish(@RequestParam("name") String streamKey) {
        Video video = streamService.onPublish(streamKey);
        String redirectUrl = "rtmp://" + rtmpServerHost + ":1935/live/" + video.getVideoUrl();
        log.info("redirecturl : " + redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    @PostMapping("/v1/onPublishDone")
    public ResponseEntity<Void> onPublishDone(@RequestParam("name") String streamKey) {
        Video video = streamService.onPublishDone(streamKey);
        log.info("Stream " + streamKey + " is closed");
        // Response is ignored
        return ResponseEntity.ok().build();
    }

}
