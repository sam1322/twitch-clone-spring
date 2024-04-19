package com.sam.twitchclone.controller.block;

import com.sam.twitchclone.controller.block.dto.BlockResponse;
import com.sam.twitchclone.controller.block.dto.BlockerDTO;
import com.sam.twitchclone.model.BaseResponse;
import com.sam.twitchclone.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @GetMapping("/v1/block/blockedList")
    public ResponseEntity<List<BlockerDTO>> getEndBlockerList() {  // ie get list of blocked users (ie end blocker) for the user
        return ResponseEntity.ok(blockService.getEndBlockerList());
    }


    @GetMapping("/v1/block/{userId}/isBlocked")
    public ResponseEntity<BaseResponse> getIsBlocking(@PathVariable UUID userId, @RequestParam Boolean byUser) { // ie get list of followers for the provided userId ie list of followers whose src following is userId
        Boolean value = blockService.getIsBlocking(userId, byUser);
        return ResponseEntity.ok(BaseResponse.builder()
                .message("User is blocking the specified user")
                .build()
        );
    }

    @PostMapping("/v1/block/{endBlockerId}/block")
    public ResponseEntity<BlockResponse> blockUser(@PathVariable UUID endBlockerId) {
        BlockResponse follower = blockService.blockUser(endBlockerId);
        return ResponseEntity.ok(follower);
    }

    @DeleteMapping("/v1/block/{endBlockerId}/unblock")
    public ResponseEntity<BlockResponse> unBlockUser(@PathVariable UUID endBlockerId) {
        BlockResponse follower = blockService.unBlockUser(endBlockerId);
        return ResponseEntity.ok(follower);
    }
}
