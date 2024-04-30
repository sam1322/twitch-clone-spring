package com.sam.twitchclone.mapper;

import com.sam.twitchclone.controller.user.dto.UserDetail;
import com.sam.twitchclone.controller.user.dto.UserResponse;
import com.sam.twitchclone.dao.postgres.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.fullName")
    UserResponse userToUserResponse(User user);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.fullName")
    UserDetail userToUserDetail(User user);

    List<UserResponse> usertoUserResponseList(List<User> userList);
}
