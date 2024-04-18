package com.sam.twitchclone.controller.user;

import com.sam.twitchclone.controller.user.dto.UserResponse;
import com.sam.twitchclone.service.SecurityService;
import com.sam.twitchclone.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final SecurityService securityService;

    @GetMapping("/v1/user/userId/{userId}")
    public ResponseEntity<UserResponse> getUserByUserId(@PathVariable String userId) {
        UserResponse userDetail = userService.getUserDetail(userId);
        return ResponseEntity.ok(userDetail);
    }

    @GetMapping("/v1/user/detail")
    public ResponseEntity<UserResponse> getUserDetails() {
        String userId = securityService.getUserInfo().getUserId();
        UserResponse userDetail = userService.getUserDetail(userId);
        return ResponseEntity.ok(userDetail);
    }

    @GetMapping("/v1/user/recommendations")
    public ResponseEntity<List<UserResponse>> getRecommendations() {
        List<UserResponse> userDetailList = userService.getUserList();
        return ResponseEntity.ok(userDetailList);
    }

    @GetMapping("/v1/user/username/{userName}")
    public ResponseEntity<UserResponse> getUserByName(@PathVariable String userName) {
        UserResponse userDetail = userService.getUserByName(userName);
        return ResponseEntity.ok(userDetail);
    }

}
