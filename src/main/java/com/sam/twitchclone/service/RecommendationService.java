package com.sam.twitchclone.service;

import com.sam.twitchclone.controller.stream.dto.StreamResponse;
import com.sam.twitchclone.controller.user.dto.UserResponse;
import com.sam.twitchclone.dao.postgres.model.Stream;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.mapper.StreamMapper;
import com.sam.twitchclone.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final FollowerService followerService;
    private final SecurityService securityService;
    private final StreamMapper streamMapper;

    public List<UserResponse> getRecommendationList() {
        String userId = securityService.getUserInfo().getUserId();
        List<User> followingUsers;
        if (userId != null) {
            User currentUser = userService.getUser(UUID.fromString(userId));
            followingUsers = followerService.getEndFollowerList(currentUser.getId());
            followingUsers.add(currentUser);
        } else {
            followingUsers = new ArrayList<>();
        }


        System.out.println("userId : " + userId);
        // todo - only give users excluding the userId from which the api is called
        List<User> allUsers = userService.getAllUsers();
        List<User> recommendedUsers = allUsers.stream()
                .filter(user -> !followingUsers.contains(user))
                .collect(Collectors.toList());
        if (recommendedUsers.isEmpty()) {
            throw new IllegalArgumentException("No user found");
        }

        List<UserResponse> userResponses = userMapper.usertoUserResponseList(recommendedUsers);
        for (int i = 0; i < recommendedUsers.size(); ++i) {
            Stream stream = userService.getCurrentStream(recommendedUsers.get(i));
            if (stream != null) {
                StreamResponse streamResponse = streamMapper.streamToStreamResponse(stream);
                if (stream.getLatestVideo() != null) {
                    streamResponse.setCurrentVideo(streamMapper.videoToVideoResponse(stream.getLatestVideo()));
                }
//                if (stream.getVideo() != null) {
//                    streamResponse.setCurrentVideo(streamMapper.videoToVideoResponse(stream.getVideo()));
//                }
                userResponses.get(i).setCurrentStream(streamResponse);
            }
//            streamResponse.setCurrentVideo(streamMapper.videoToVideoResponse(streamService.getLatestVideo(stream.getVideo())));
//            userResponses.get(i).setCurrentStream(streamMapper.streamToStreamResponse(recommendedUsers.get(i).getCurrentStream()));
        }


        return userResponses;
//        return userMapper.userToUserDetail(user.get());
    }

    private StreamResponse getCurrentStreamFromStreamList(List<Stream> streams) {
        if (streams.isEmpty()) return null;
//        streams.sort(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Stream> newStream = streams.stream()
                .sorted(Comparator.comparing(Stream::getCreatedAt).reversed())
                .toList();
        return streamMapper.streamToStreamResponse(newStream.get(0));
    }
}
