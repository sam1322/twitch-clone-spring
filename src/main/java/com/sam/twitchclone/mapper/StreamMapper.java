package com.sam.twitchclone.mapper;

import com.sam.twitchclone.controller.stream.dto.StreamResponse;
import com.sam.twitchclone.controller.stream.dto.VideoResponse;
import com.sam.twitchclone.dao.postgres.model.Stream;
import com.sam.twitchclone.dao.postgres.model.Video;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StreamMapper {
    //    @Mapping(target = "blockerId", source = "blocker.id")
    StreamResponse streamToStreamResponse(Stream stream);

    VideoResponse videoToVideoResponse(Video video);
}
