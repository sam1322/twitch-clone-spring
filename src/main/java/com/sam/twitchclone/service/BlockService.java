package com.sam.twitchclone.service;

import com.sam.twitchclone.controller.block.dto.BlockResponse;
import com.sam.twitchclone.controller.block.dto.BlockerDTO;
import com.sam.twitchclone.dao.postgres.model.Block;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.BlockRepository;
import com.sam.twitchclone.dao.postgres.repository.UserRepository;
import com.sam.twitchclone.mapper.BlockerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final BlockerMapper blockerMapper;

    public List<BlockerDTO> getEndBlockerList() {
        String userId = securityService.getUserInfo().getUserId();
        return getEndBlockerList(UUID.fromString(userId));
    }

    public List<BlockerDTO> getEndBlockerList(UUID followerId) {
        return blockRepository.findBySrcBlockerId(followerId)
                .stream()
                .map(this::mapToBlockerDTO)
                .collect(Collectors.toList());
    }

    private BlockerDTO mapToBlockerDTO(Block blocker) {
        User followedUser = userRepository.findById(blocker.getEndBlocker().getId())
                .orElseThrow(() -> new IllegalArgumentException("Blocked user not found"));

        return blockerMapper.userToBlockerDto(followedUser);

    }


    public Boolean getIsBlocking(UUID endBlockerId, boolean byUser) {
        String userId = securityService.getUserInfo().getUserId();
        UUID srcBlockerId = UUID.fromString(userId);
        if (srcBlockerId.equals(endBlockerId)) {
//            return false; // you can not block yourself
            throw new IllegalArgumentException("User did not block the specified user");
        }

        Optional<Block> existingBlocker;
        if (byUser) {
//            check whether the user has blocked the end User
            existingBlocker = blockRepository.findBySrcBlockerIdAndEndBlockerId(srcBlockerId, endBlockerId);

        } else {
//            check whether the user itself has been blocked by the end User
            existingBlocker = blockRepository.findBySrcBlockerIdAndEndBlockerId(endBlockerId, srcBlockerId);
        }
        if (existingBlocker.isEmpty()) {
            throw new IllegalArgumentException("User did not block the specified user");
        }
        return true;
    }

    public BlockResponse blockUser(UUID endBlockerId) {
        String userId = securityService.getUserInfo().getUserId();
        UUID srcBlockerId = UUID.fromString(userId);
        if (srcBlockerId.equals(endBlockerId)) {
            throw new IllegalArgumentException("You can not block yourself");
        }
        return blockUser(srcBlockerId, endBlockerId);
    }

    private BlockResponse blockUser(UUID srcBlockerId, UUID endBlockerId) {
        Optional<Block> existingBlocker = blockRepository.findBySrcBlockerIdAndEndBlockerId(srcBlockerId, endBlockerId);

        if (existingBlocker.isPresent()) {
            throw new IllegalArgumentException("User is already blocking the specified user");
        }

        User srcBlocker = userRepository.findById(srcBlockerId)
                .orElseThrow(() -> new IllegalArgumentException("Blocking user not found"));
        User endBlocker = userRepository.findById(endBlockerId)
                .orElseThrow(() -> new IllegalArgumentException("Blocked user not found"));


        Instant currentTime = Instant.now();
        Block block = Block.builder()
                .srcBlocker(srcBlocker)
                .endBlocker(endBlocker)
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .build();
        blockRepository.save(block);
        return BlockResponse.builder()
                .srcBlocker(blockerMapper.userToBlockerDto(block.getSrcBlocker()))
                .endBlocker(blockerMapper.userToBlockerDto(block.getEndBlocker()))
                .build();
    }

    public BlockResponse unBlockUser(UUID endBlockerId) {
        String userId = securityService.getUserInfo().getUserId();
        UUID srcBlockerId = UUID.fromString(userId);
        if (srcBlockerId.equals(endBlockerId)) {
            throw new IllegalArgumentException("You can not unblock yourself");
        }
        return unBlockUser(srcBlockerId, endBlockerId);
    }

    public BlockResponse unBlockUser(UUID srcBlockerId, UUID endBlockerId) {
        Block existingBlockedUser = blockRepository.findBySrcBlockerIdAndEndBlockerId(srcBlockerId, endBlockerId)
                .orElseThrow(() -> new IllegalArgumentException("User has already unblocked the specified user."));
        BlockResponse followerResponse = BlockResponse.builder()
                .srcBlocker(blockerMapper.userToBlockerDto(existingBlockedUser.getSrcBlocker()))
                .endBlocker(blockerMapper.userToBlockerDto(existingBlockedUser.getEndBlocker()))
                .build();
        blockRepository.delete(existingBlockedUser);
        return followerResponse;
    }


}
