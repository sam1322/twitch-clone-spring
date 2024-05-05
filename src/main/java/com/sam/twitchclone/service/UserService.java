package com.sam.twitchclone.service;

import com.sam.twitchclone.controller.stream.dto.StreamResponse;
import com.sam.twitchclone.controller.user.dto.UserDetail;
import com.sam.twitchclone.controller.user.dto.UserRequest;
import com.sam.twitchclone.controller.user.dto.UserResponse;
import com.sam.twitchclone.dao.postgres.model.Stream;
import com.sam.twitchclone.dao.postgres.model.Video;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.UserRepository;
import com.sam.twitchclone.mapper.StreamMapper;
import com.sam.twitchclone.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final StreamMapper streamMapper;
    private final SecurityService securityService;


    public User getUser(UUID userId) {
        checkArgument(userId != null, "user id cannot be null");

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        return user.get();
    }

    public UserDetail getUserDetail(UUID userId) {
        checkArgument(userId != null, "user id cannot be null");

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        return userMapper.userToUserDetail(user.get());
    }


    public UserResponse getUserByName(String userName) {
        checkArgument(userName != null, "username cannot be null");

        Optional<User> user = userRepository.findByFullNameIgnoreCase(userName);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }
        User user1 = user.get();
        UserResponse userResponse = userMapper.userToUserResponse(user1);
        Stream stream = user1.getLatestStream();
        if (stream != null) {
            Video video = stream.getLatestVideo();
            StreamResponse streamResponse = streamMapper.streamToStreamResponse(stream);
            streamResponse.setCurrentVideo(streamMapper.videoToVideoResponse(video));
            userResponse.setCurrentStream(streamResponse);
        }
        return userResponse;
    }

    public UserResponse getUserDetail(String userId) {
        checkArgument(userId != null, "user id cannot be null");

        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if (user.isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }

        return userMapper.userToUserResponse(user.get());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Stream getCurrentStream(User user) {
        List<Stream> streams = user.getStreamList();
        if (streams.isEmpty()) return null;
//        streams.sort(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Stream> newStream = streams.stream()
                .sorted(Comparator.comparing(Stream::getCreatedAt).reversed())
                .collect(Collectors.toList());
        return newStream.get(0);
    }

    public UserDetail updateUserData(UserRequest userRequest) {
        String userId = securityService.getUserInfo().getUserId();
        User user = getUser(UUID.fromString(userId));

//
//        Optional<User> user1= userRepository.findById(userId);
//        if(user1.isEmpty()){
//            throw new IllegalArgumentException("User not found");
//        }
//
//        User user = user1.get();

        if (userRequest.getUserName() != null) {
            Optional<User> user2 = userRepository.findByFullNameIgnoreCase(userRequest.getUserName());
            if (user2.isPresent() && !user2.get().getId().equals(userId)) {
                throw new IllegalArgumentException("Username already exists");
            }
            user.setFullName(userRequest.getUserName());
        }

        if (userRequest.getBio() != null) {
            user.setBio(userRequest.getBio());
        }
//        user.setUserImage(userRequest.getUserImage());
        userRepository.save(user);
        return userMapper.userToUserDetail(user);
    }

//    public UserDetail uploadUserImage(MultipartFile userImage) {
//        if (userImage.getSize() > 2 * 1024 * 1024) {
//            throw new IllegalArgumentException("Image size exceeds the limit of 2MB");
//        }
//
//        String userId = securityService.getUserInfo().getUserId();
//        User user = getUser(UUID.fromString(userId));
//
//
//    }

}
