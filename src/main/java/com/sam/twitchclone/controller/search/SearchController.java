package com.sam.twitchclone.controller.search;

import com.sam.twitchclone.controller.search.dto.SearchRequest;
import com.sam.twitchclone.controller.stream.dto.VideoResponse;
import com.sam.twitchclone.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class SearchController {
    private final SearchService searchService;

    @PostMapping("/v1/search")
    public ResponseEntity<List<VideoResponse>> getResults(@RequestBody SearchRequest request) {
        List<VideoResponse> videoResponseList = searchService.searchVideos(request.getTerm());
        return ResponseEntity.ok(videoResponseList);
    }
}
