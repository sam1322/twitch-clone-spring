package com.sam.twitchclone.service;

import com.sam.twitchclone.controller.follower.dto.FollowerDTO;
import com.sam.twitchclone.controller.follower.dto.FollowerResponse;
import com.sam.twitchclone.dao.postgres.model.Follower;
import com.sam.twitchclone.dao.postgres.model.Stream;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.FollowerRepository;
import com.sam.twitchclone.dao.postgres.repository.StreamRepository;
import com.sam.twitchclone.dao.postgres.repository.UserRepository;
import com.sam.twitchclone.mapper.FollowerMapper;
import com.sam.twitchclone.mapper.StreamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowerService {
    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final SecurityService securityService;
    private final FollowerMapper followerMapper;
    private final StreamRepository streamRepository;
    private final StreamMapper streamMapper;

    //    public List<User> getFollowers(UUID followedId) {

    public List<User> getEndFollowerList(UUID srcFollowerId) { // ie get the list of users the current user is following
        return followerRepository.findBySrcFollowerId(srcFollowerId)
                .stream()
                .map(Follower::getEndFollower)
                .collect(Collectors.toList());
    }

    public List<FollowerDTO> getFollowers(UUID followedId) {
        return followerRepository.findByEndFollowerId(followedId)
                .stream()
                .map(this::mapToFollowerDTO)
                .collect(Collectors.toList());
    }

    public List<FollowerDTO> getFollowersList() {
        String userId = securityService.getUserInfo().getUserId();
        return getFollowers(UUID.fromString(userId));
    }

    public List<FollowerDTO> getFollowingList() {
        String userId = securityService.getUserInfo().getUserId();
        return getFollowing(UUID.fromString(userId));
    }

    public List<FollowerDTO> getFollowing(UUID followerId) {
        return followerRepository.findBySrcFollowerId(followerId)
                .stream()
                .map(this::mapToFollowingDTO)
                .collect(Collectors.toList());
    }

    public Boolean getIsFollowing(UUID endFollowerId) {
        String userId = securityService.getUserInfo().getUserId();
        UUID srcFollowerId = UUID.fromString(userId);
        if (srcFollowerId.equals(endFollowerId)) {
            return true;
        }
        Optional<Follower> existingFollower = followerRepository.findBySrcFollowerIdAndEndFollowerId(srcFollowerId, endFollowerId);
        if (existingFollower.isEmpty()) {
            throw new IllegalArgumentException("User does not follow the specified user");
        }
        return true;
    }

    public Follower followUser(User srcFollower, User endFollower) {
        Instant currentTime = Instant.now();
        Follower newFollower = Follower.builder()
                .srcFollower(srcFollower)
                .endFollower(endFollower)
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .build();

        return followerRepository.save(newFollower);
    }

    public FollowerResponse followUser(UUID srcFollowerId, UUID endFollowerId) {
        Optional<Follower> existingFollower = followerRepository.findBySrcFollowerIdAndEndFollowerId(srcFollowerId, endFollowerId);

        if (existingFollower.isPresent()) {
            throw new IllegalArgumentException("User is already following to the specified user");
        }

        User srcFollower = userRepository.findById(srcFollowerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower user not found"));
        User endFollower = userRepository.findById(endFollowerId)
                .orElseThrow(() -> new IllegalArgumentException("Followed user not found"));

        Follower follower = followUser(srcFollower, endFollower);
        FollowerResponse followerResponse = FollowerResponse.builder()
                .srcFollower(followerMapper.userToFollowerDto(follower.getSrcFollower()))
                .endFollower(followerMapper.userToFollowerDto(follower.getEndFollower()))
                .build();
        return followerResponse;
    }

    public FollowerResponse followUser(UUID endFollowerId) {
        String userId = securityService.getUserInfo().getUserId();
        UUID srcFollowerId = UUID.fromString(userId);
        if (srcFollowerId.equals(endFollowerId)) {
            throw new IllegalArgumentException("Already followed");
        }
        return followUser(srcFollowerId, endFollowerId);
    }


    public FollowerResponse unfollowUser(UUID srcFollowerId, UUID endFollowerId) {
        Follower existingFollower = followerRepository.findBySrcFollowerIdAndEndFollowerId(srcFollowerId, endFollowerId)
                .orElseThrow(() -> new IllegalArgumentException("User is already unfollowing the specified user."));
        FollowerResponse followerResponse = FollowerResponse.builder()
                .srcFollower(followerMapper.userToFollowerDto(existingFollower.getSrcFollower()))
                .endFollower(followerMapper.userToFollowerDto(existingFollower.getEndFollower()))
                .build();
        followerRepository.delete(existingFollower);
        return followerResponse;
    }

    public FollowerResponse unfollowUser(UUID endFollowerId) {
        String userId = securityService.getUserInfo().getUserId();
        UUID srcFollowerId = UUID.fromString(userId);
        if (srcFollowerId.equals(endFollowerId)) {
            throw new IllegalArgumentException("You can not unfollow yourself");
        }
        return unfollowUser(srcFollowerId, endFollowerId);
    }

    private FollowerDTO mapToFollowingDTO(Follower follower) {
//        User followedUser = userRepository.findById(follower.getEndFollower().getId())
//                .orElseThrow(() -> new IllegalArgumentException("Followed user not found"));
        User followedUser = userService.getUser(follower.getEndFollower().getId());

        FollowerDTO followerDTO = followerMapper.userToFollowerDto(followedUser);
        Optional<Stream> currentStream = streamRepository.findFirstByUserIdOrderByCreatedAtDesc(followedUser.getId());
        currentStream.ifPresent(stream -> followerDTO.setCurrentStream(streamMapper.streamToStreamResponse(stream)));
//        currentStream.ifPresent(stream -> followerDTO.setLive(stream.isLive()));
//        currentStream.ifPresent(followerDTO::setCurrentStream);
        return followerDTO;
    }


    private FollowerDTO mapToFollowerDTO(Follower follower) {
//        User followerUser = userRepository.findById(follower.getSrcFollower().getId())
//                .orElseThrow(() -> new IllegalArgumentException("Follower user not found"));

        User followerUser = userService.getUser(follower.getSrcFollower().getId());

        return followerMapper.userToFollowerDto(followerUser);

    }
}