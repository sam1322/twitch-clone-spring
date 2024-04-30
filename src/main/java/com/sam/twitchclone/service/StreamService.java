package com.sam.twitchclone.service;

import com.sam.twitchclone.constant.enums.VideoStatus;
import com.sam.twitchclone.controller.stream.dto.StreamRequest;
import com.sam.twitchclone.controller.stream.dto.StreamResponse;
import com.sam.twitchclone.controller.stream.dto.VideoResponse;
import com.sam.twitchclone.dao.postgres.model.Stream;
import com.sam.twitchclone.dao.postgres.model.Video;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.StreamRepository;
import com.sam.twitchclone.dao.postgres.repository.VideoRepository;
import com.sam.twitchclone.mapper.StreamMapper;
import com.sam.twitchclone.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StreamService {
    private final SecurityService securityService;
    private final StreamRepository streamRepository;
    private final VideoRepository videoRepository;
    private final UserService userService;
    private final StreamMapper streamMapper;
    private final UserMapper userMapper;

    public List<VideoResponse> getListofVideos() {
//        String userId = securityService.getUserInfo().getUserId();

//        List<Video> videoList = videoRepository.findVideoByUserId(UUID.fromString(userId), Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Video> videoList = videoRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<VideoResponse> videoResponseList = new ArrayList<>();
        for (Video video : videoList) {
            videoResponseList.add(VideoResponse.builder()
                    .videoId(video.getId())
                    .createdAt(video.getCreatedAt())
                    .updatedAt(video.getUpdatedAt())
                    .videoUrl(video.getVideoUrl())
                    .videoStatus(video.getVideoStatus())
                    .user(userMapper.userToUserDetail(video.getUser()))
                    .build());
        }

        return videoResponseList;
    }

    public VideoResponse getVideo(UUID videoID) {
        Optional<Video> video1 = videoRepository.findById(videoID);
        if (video1.isEmpty()) {
            throw new IllegalArgumentException("No such video found");
        }

        Video video = video1.get();
        return VideoResponse.builder()
                .videoId(video.getId())
                .createdAt(video.getCreatedAt())
                .updatedAt(video.getUpdatedAt())
                .videoUrl(video.getVideoUrl())
                .videoStatus(video.getVideoStatus())
                .user(userMapper.userToUserDetail(video.getUser()))
                .build();
    }


    public StreamResponse generateStreamKey() {
        String userId = securityService.getUserInfo().getUserId();
        User user = userService.getUser(UUID.fromString(userId));
        Instant currentTime = Instant.now();

//        Optional<List<Stream>> oldStreams = streamRepository.findByUserIdAndStatus(UUID.fromString(userId), StreamStatus.PAUSED);
        List<Stream> streamList = streamRepository.findByUserIdAndLiveOrUserIdAndExpired(UUID.fromString(userId), true, UUID.fromString(userId), false, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (!streamList.isEmpty()) {
            for (Stream stream : streamList) {
                stream.setLive(false);
                stream.setExpired(true);
                stream.setUpdatedAt(currentTime);
            }
            streamRepository.saveAll(streamList);
        }
        Optional<Stream> prevStream = streamRepository.findFirstByUserIdOrderByCreatedAtDesc(UUID.fromString(userId));
        boolean chatEnabled = true;
        boolean chatDelayed = false;
        boolean chatFollowerOnly = false;

        if (prevStream.isPresent()) {
            chatEnabled = prevStream.get().isChatEnabled();
            chatDelayed = prevStream.get().isChatDelayed();
            chatFollowerOnly = prevStream.get().isChatFollowerOnly();
        }

        Stream stream = Stream.builder()
//                .status(StreamStatus.PAUSED)
                .live(false)
                .expired(false)
                .chatEnabled(chatEnabled)
                .chatDelayed(chatDelayed)
                .chatFollowerOnly(chatFollowerOnly)
                .user(user)
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .build();

        stream = streamRepository.save(stream);
//        user.setCurrentStream(stream);
//        userService.saveUser(user);

//        return uuidToBase64(String.valueOf(stream.getId()));
        StreamResponse streamResponse = streamMapper.streamToStreamResponse(stream);
        streamResponse.setStreamKey(uuidToBase64(String.valueOf(stream.getId())));
        return streamResponse;
    }

    public StreamResponse getStreamKey() {
        String userId = securityService.getUserInfo().getUserId();
//        Optional<List<Stream>> stream = streamRepository.findByUserIdAndStatus(UUID.fromString(userId), StreamStatus.PAUSED);
        Optional<Stream> stream = streamRepository.findFirstByUserIdOrderByCreatedAtDesc(UUID.fromString(userId));

        if (stream.isEmpty()) {
            throw new IllegalArgumentException("No stream key found");
        }
//        List<Stream> streamList = stream.get();
//        return uuidToBase64(String.valueOf(stream.get().getId()));
        StreamResponse streamResponse = streamMapper.streamToStreamResponse(stream.get());
        streamResponse.setStreamKey(uuidToBase64(String.valueOf(stream.get().getId())));
        streamResponse.setCurrentVideo(streamMapper.videoToVideoResponse(stream.get().getLatestVideo()));
//        streamResponse.setCurrentVideo(streamMapper.videoToVideoResponse(stream.get().getVideo()));
//        streamResponse.setCurrentVideo(streamMapper.videoToVideoResponse(getLatestVideo(stream.get().getVideo())));
        return streamResponse;
    }

    public StreamResponse updateStreamKey(StreamRequest request) {
//        String streamId = uuidFromBase64(request.getStreamKey());
        String userId = securityService.getUserInfo().getUserId();

        Optional<Stream> stream = streamRepository.findFirstByUserIdOrderByCreatedAtDesc(UUID.fromString(userId));
        if (stream.isEmpty()) {
            throw new IllegalArgumentException("No such stream key exist");
        }
        Stream stream1 = stream.get();
        Instant currentTime = Instant.now();
        stream1.setUpdatedAt(currentTime);
//        stream1.setLive(request.isLive());
        stream1.setChatDelayed(request.isChatDelayed());
        stream1.setChatEnabled(request.isChatEnabled());
        stream1.setChatFollowerOnly(request.isChatFollowerOnly());

        stream1 = streamRepository.save(stream1);

        StreamResponse streamResponse = streamMapper.streamToStreamResponse(stream1);
        streamResponse.setStreamKey(uuidToBase64(String.valueOf(stream1.getId())));
        return streamResponse;
    }

    public StreamResponse verifyKey(String streamKey) {
        String streamId = uuidFromBase64(streamKey);

        Optional<Stream> stream = streamRepository.findByIdAndExpiredIsFalse(UUID.fromString(streamId));
        if (stream.isEmpty()) {
            throw new IllegalArgumentException("Invalid Stream Key");
        }
        Stream stream1 = stream.get();
        return StreamResponse.builder()
                .createdAt(stream1.getCreatedAt())
                .updatedAt(stream1.getUpdatedAt())
                .live(stream1.isLive())
                .streamKey(String.valueOf(stream1.getId()))
                .chatEnabled(stream1.isChatEnabled())
                .chatDelayed(stream1.isChatDelayed())
                .chatFollowerOnly(stream1.isChatFollowerOnly())
                .build();

    }

    public Video onPublish(String streamKey) {
        String streamUid = uuidFromBase64(streamKey);

        Optional<Stream> stream = streamRepository.findByIdAndExpiredIsFalse(UUID.fromString(streamUid));
        if (stream.isEmpty()) {
            throw new IllegalArgumentException("Invalid Stream Key");
        }
        Stream stream1 = stream.get();
        Instant currentTIme = Instant.now();
        Video video = Video.builder()
                .createdAt(currentTIme)
                .updatedAt(currentTIme)
                .stream(stream1)
                .user(stream1.getUser())
                .videoStatus(VideoStatus.LIVE)
                .videoUrl(streamUid + "_" + currentTIme.toEpochMilli())
//                .videoUrl(currentTIme.toString())
//                .videoUrl(streamUid + "/" + currentTIme.toString())
                .build();

        Video video1 = videoRepository.save(video);
//        stream1.setVideo(video1);
//        stream1.setLive(true);
//        stream1.setUpdatedAt(currentTIme);

//        streamRepository.save(stream1);
        return video;
    }

    public Video onPublishDone(String streamKey) {
        String streamUid = uuidFromBase64(streamKey);

        Optional<Stream> stream = streamRepository.findById(UUID.fromString(streamUid));
        if (stream.isEmpty()) {
            throw new IllegalArgumentException("No such stream key exist");
        }
        Stream stream1 = stream.get();
        Instant currentTIme = Instant.now();

//        List<Video> videoList = stream1.getVideo();
//        Video video = getLatestVideo(videoList);
//        if(video == null){
//            throw new IllegalArgumentException("No video found");
//        }
        Video video = stream1.getLatestVideo();
//        Video video = stream1.getVideo();
        video.setUpdatedAt(currentTIme);
        video.setVideoStatus(VideoStatus.ENDED);
        videoRepository.save(video);

        stream1.setLive(false);
        stream1.setUpdatedAt(currentTIme);
        streamRepository.save(stream1);
        return video;
    }

    public Video getLatestVideo(List<Video> videoList) {
        if (videoList.isEmpty()) return null;
//        streams.sort(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Video> newStream = videoList.stream()
                .sorted(Comparator.comparing(Video::getCreatedAt).reversed())
                .toList();
        return newStream.get(0);
    }

    private String uuidToBase64(String str) {
        UUID uuid = UUID.fromString(str);
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return Base64.getUrlEncoder().encodeToString(bb.array());
    }

    private String uuidFromBase64(String str) {
        byte[] decodedKey = Base64.getUrlDecoder().decode(str);
        ByteBuffer bb = ByteBuffer.wrap(decodedKey);
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid.toString();
    }

}
