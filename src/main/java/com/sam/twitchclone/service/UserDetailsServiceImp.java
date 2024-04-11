package com.sam.twitchclone.service;

import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserRepository userRepository;

//    @Override
//    public User loadUserByUsername(String userId) throws UsernameNotFoundException {
//        return userRepository.findById(UUID.fromString(userId)).orElseThrow(()->new UsernameNotFoundException("User not found"));
//    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    public User loadUserByUserId(UUID userId) throws UsernameNotFoundException {
        return userRepository.findById(userId).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

}
