package com.sam.twitchclone.service;

import com.sam.twitchclone.controller.user.dto.UserResponse;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        return userMapper.usertoUserResponseList(recommendedUsers);
//        return userMapper.userToUserDetail(user.get());
    }
}
