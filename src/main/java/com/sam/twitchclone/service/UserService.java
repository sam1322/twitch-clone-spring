package com.sam.twitchclone.service;

import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.UserRepository;
import com.sam.twitchclone.model.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public  User getUser(String userId) {
        checkArgument(userId != null, "user id cannot be null");

        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if (user.isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }

       return user.get();
    }
}
