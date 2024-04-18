package com.sam.twitchclone.mapper;

import com.sam.twitchclone.controller.follower.dto.FollowerDTO;
import com.sam.twitchclone.dao.postgres.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FollowerMapper {

    @Mapping(target = "followerId",source = "follower.id")
    FollowerDTO userToFollowerDto(User follower);

}
