package com.sam.twitchclone.mapper;

import com.sam.twitchclone.controller.stream.dto.StreamResponse;
import com.sam.twitchclone.controller.stream.dto.VideoResponse;
import com.sam.twitchclone.dao.postgres.model.Stream;
import com.sam.twitchclone.dao.postgres.model.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = {UserMapper.class})
public interface StreamMapper {
    //    @Mapping(target = "blockerId", source = "blocker.id")
    StreamResponse streamToStreamResponse(Stream stream);

    @Mapping(target = "videoId", source = "video.id")
//    @Mapping(target = "user", source = "video.user", qualifiedByName = "userToUserDetail")
    VideoResponse videoToVideoResponse(Video video);

    List<VideoResponse> videoListToVideoResponseList(List<Video> videoList);
}
