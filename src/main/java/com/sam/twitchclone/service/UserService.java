package com.sam.twitchclone.service;

import com.sam.twitchclone.controller.stream.dto.StreamResponse;
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


    public User getUser(UUID userId) {
        checkArgument(userId != null, "user id cannot be null");

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
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

}
