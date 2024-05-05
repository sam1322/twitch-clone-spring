package com.sam.twitchclone.controller.user;

import com.sam.twitchclone.controller.user.dto.UserDetail;
import com.sam.twitchclone.controller.user.dto.UserRequest;
import com.sam.twitchclone.controller.user.dto.UserResponse;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.model.BaseResponse;
import com.sam.twitchclone.service.FollowerService;
import com.sam.twitchclone.service.SecurityService;
import com.sam.twitchclone.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final SecurityService securityService;
    private final FollowerService followerService;

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

//    @GetMapping("/v1/user/recommendations")
//    public ResponseEntity<List<UserResponse>> getRecommendations() {
//        List<UserResponse> userDetailList = userService.getUserList();
//        return ResponseEntity.ok(userDetailList);
//    }

    @GetMapping("/v1/user/username/{userName}")
    public ResponseEntity<UserResponse> getUserByName(@PathVariable String userName) {
        UserResponse userDetail = userService.getUserByName(userName);
        return ResponseEntity.ok(userDetail);
    }

    @GetMapping("/v1/user")
    public ResponseEntity<User> getUser() {
        String userId = securityService.getUserInfo().getUserId();

        User userDetail = userService.getUser(UUID.fromString(userId));
        return ResponseEntity.ok(userDetail);
    }

    @PutMapping("/v1/user/updateUser")
    public ResponseEntity<UserDetail> updateUser( @RequestBody UserRequest user) {
//        String userId = securityService.getUserInfo().getUserId();
        UserDetail updatedUser = userService.updateUserData( user);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/v1/user/userImage/upload")
    public ResponseEntity<BaseResponse> uploadImage(@RequestParam("userImage") MultipartFile userImage) {
        // Process the uploaded image
        // ...
        return ResponseEntity.ok(BaseResponse.builder().message("Image uploaded successfully").build());
    }

}
