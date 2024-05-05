package com.sam.twitchclone.service;

import com.sam.twitchclone.controller.stream.dto.VideoResponse;
import com.sam.twitchclone.dao.postgres.model.Video;
import com.sam.twitchclone.dao.postgres.repository.VideoRepository;
import com.sam.twitchclone.mapper.StreamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final VideoRepository videoRepository;
    private final StreamMapper streamMapper;

    public List<VideoResponse> searchVideos(String term) {
        List<Video> videoList = videoRepository.findVideoByTitleContainingAnyWord(term);
        return streamMapper.videoListToVideoResponseList(videoList);
    }
}
