package com.sam.twitchclone.mapper;

import com.sam.twitchclone.controller.block.dto.BlockerDTO;
import com.sam.twitchclone.dao.postgres.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BlockerMapper {
    @Mapping(target = "blockerId", source = "blocker.id")
    BlockerDTO userToBlockerDto(User blocker);
}
