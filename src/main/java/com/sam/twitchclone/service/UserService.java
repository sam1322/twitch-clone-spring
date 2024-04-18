package com.sam.twitchclone.service;

import com.sam.twitchclone.controller.user.dto.UserResponse;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.UserRepository;
import com.sam.twitchclone.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityService securityService;

    public User getUser(String userId) {
        checkArgument(userId != null, "user id cannot be null");

        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if (user.isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }

        return user.get();
    }

    public UserResponse getUserByName(String userName) {
        checkArgument(userName != null, "username cannot be null");

        Optional<User> user = userRepository.findByFullNameIgnoreCase(userName);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }
        User user1 = user.get();
        return userMapper.userToUserResponse(user1);
    }

    public UserResponse getUserDetail(String userId) {
        checkArgument(userId != null, "user id cannot be null");

        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if (user.isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }

        return userMapper.userToUserResponse(user.get());
//        return userMapper.userToUserDetail(user.get());
    }

    public List<UserResponse> getUserList() {
        String userId = securityService.getUserInfo().getUserId();
        System.out.println("userId : " + userId);
        // todo - only give users excluding the userId from which the api is called
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new IllegalArgumentException("No user found");
        }

        return userMapper.usertoUserResponseList(userList);
//        return userMapper.userToUserDetail(user.get());
    }


}
