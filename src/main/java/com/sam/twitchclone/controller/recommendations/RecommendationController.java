package com.sam.twitchclone.controller.recommendations;

import com.sam.twitchclone.controller.user.dto.UserResponse;
import com.sam.twitchclone.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/v1/user/recommendations")
    public ResponseEntity<List<UserResponse>> getRecommendations() {
        List<UserResponse> userDetailList = recommendationService.getRecommendationList();
        return ResponseEntity.ok(userDetailList);
    }
}
