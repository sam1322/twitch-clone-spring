package com.sam.twitchclone.controller.follower;

import com.sam.twitchclone.controller.follower.dto.FollowerDTO;
import com.sam.twitchclone.controller.follower.dto.FollowerResponse;
import com.sam.twitchclone.model.BaseResponse;
import com.sam.twitchclone.service.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class FollowerController {
    private final FollowerService followerService;


//    @GetMapping("/v1/follower/{userId}/followers")
//    public List<FollowerDTO> getFollowers(@PathVariable UUID userId) {  // ie get list of followers for the provided userId ie list of followers whose end follower is userId
//        return followerService.getFollowers(userId);
//    }

//    @GetMapping("/v1/follower/followerList")
//    public ResponseEntity<List<FollowerDTO>> getFollowersList() {  // ie get list of followers for the provided userId ie list of followers whose end follower is userId
//        return ResponseEntity.ok(followerService.getFollowersList());
//    }


//    @GetMapping("/v1/follower/{userId}/following")
//    public List<FollowerDTO> getFollowing(@PathVariable UUID userId) { // ie get list of followers for the provided userId ie list of followers whose src following is userId
//        return followerService.getFollowing(userId);
//    }

    @GetMapping("/v1/follower/followingList")
    public ResponseEntity<List<FollowerDTO>> getFollowingList() {  // ie get list of followers for the provided userId ie list of followers whose end follower is userId
        return ResponseEntity.ok(followerService.getFollowingList());
    }


    @GetMapping("/v1/follower/{userId}/isfollowing")
    public ResponseEntity<BaseResponse> getIsFollowing(@PathVariable UUID userId) { // ie get list of followers for the provided userId ie list of followers whose src following is userId
        Boolean value = followerService.getIsFollowing(userId);
        return ResponseEntity.ok(BaseResponse.builder()
                .message("User is following the user")
                .build()
        );
    }

    @PostMapping("/v1/follower/{endFollowerId}/follow")
    public ResponseEntity<FollowerResponse> followUser(@PathVariable UUID endFollowerId) {
        FollowerResponse follower = followerService.followUser(endFollowerId);
        return ResponseEntity.ok(follower);
    }

    @DeleteMapping("/v1/follower/{endFollowerId}/unfollow")
    public ResponseEntity<FollowerResponse> unfollowUser(@PathVariable UUID endFollowerId) {
        FollowerResponse follower = followerService.unfollowUser(endFollowerId);
        return ResponseEntity.ok(follower);
    }

}
