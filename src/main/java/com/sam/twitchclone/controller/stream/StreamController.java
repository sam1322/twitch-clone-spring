package com.sam.twitchclone.controller.stream;

import com.sam.twitchclone.controller.stream.dto.StreamRequest;
import com.sam.twitchclone.controller.stream.dto.StreamResponse;
import com.sam.twitchclone.controller.stream.dto.VideoRequest;
import com.sam.twitchclone.controller.stream.dto.VideoResponse;
import com.sam.twitchclone.dao.postgres.model.Video;
import com.sam.twitchclone.service.StreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Slf4j
public class StreamController {
    private final StreamService streamService;
    @Value("${rtmp.server.host}")
    private String rtmpServerHost;

    @PostMapping("/v1/stream/generateKey")
    public ResponseEntity<StreamResponse> generateStreamKey() {

        StreamResponse streamResponse = streamService.generateStreamKey();
        return ResponseEntity.ok(streamResponse);

    }

    @GetMapping("/v1/stream/streamKey")
    public ResponseEntity<StreamResponse> getCurrentStreamKey() {

        StreamResponse streamResponse = streamService.getStreamKey();
        return ResponseEntity.ok(streamResponse);
    }

    @PutMapping("/v1/stream/updateStreamKey")
    public ResponseEntity<StreamResponse> updateStreamKey(@RequestBody StreamRequest request) {

        StreamResponse streamResponse = streamService.updateStreamKey(request);
        return ResponseEntity.ok(streamResponse);
    }

    @PostMapping("/v1/stream/validateStreamkey")
    public ResponseEntity<StreamResponse> validateStreamKey(@RequestParam String streamKey) {
        StreamResponse streamResponse = streamService.verifyKey(streamKey);
        return ResponseEntity.ok(streamResponse);
    }

    @PostMapping("/v1/stream/onPublish")
    public ResponseEntity<Void> onPublish(@RequestParam("name") String streamKey) {
        Video video = streamService.onPublish(streamKey);
        String redirectUrl = "rtmp://" + rtmpServerHost + ":1935/live/" + video.getVideoUrl();
        log.info("redirecturl : " + redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    @PostMapping("/v1/stream/onPublishDone")
    public ResponseEntity<Void> onPublishDone(@RequestParam("name") String streamKey) {
        Video video = streamService.onPublishDone(streamKey);
        log.info("Stream " + streamKey + " is closed");
        // Response is ignored
        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/stream/videos/all")
    public ResponseEntity<List<VideoResponse>> getListOfVideos() {
        return ResponseEntity.ok(streamService.getListofVideos());
    }

    @GetMapping("/v1/stream/videos/{videoId}")
    public ResponseEntity<VideoResponse> getVideo(@PathVariable UUID videoId) {
        return ResponseEntity.ok(streamService.getVideo(videoId));
    }

    @PutMapping("/v1/stream/videos/{videoId}")
    public ResponseEntity<VideoResponse> updateVideo(@PathVariable UUID videoId, @RequestBody VideoRequest request) {
        return ResponseEntity.ok(streamService.updateVideo(videoId, request));
    }

}
