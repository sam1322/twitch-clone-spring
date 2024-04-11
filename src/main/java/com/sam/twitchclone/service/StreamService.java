package com.sam.twitchclone.service;

import com.sam.twitchclone.constant.enums.StreamStatus;
import com.sam.twitchclone.constant.enums.VideoStatus;
import com.sam.twitchclone.controller.stream.dto.StreamResponse;
import com.sam.twitchclone.dao.postgres.model.Stream;
import com.sam.twitchclone.dao.postgres.model.Video;
import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.StreamRepository;
import com.sam.twitchclone.dao.postgres.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StreamService {
    private final SecurityService securityService;
    private final StreamRepository streamRepository;
    private final VideoRepository videoRepository;
    private final UserService userService;

    public String generateStreamKey() {
        String userId = securityService.getUserInfo().getUserId();
        User user = userService.getUser(userId);
        Instant currentTime = Instant.now();

        Optional<List<Stream>> oldStreams = streamRepository.findByUserIdAndStatus(UUID.fromString(userId), StreamStatus.PAUSED);
        if (oldStreams.isPresent() && !oldStreams.get().isEmpty()) {
            List<Stream> streamList = oldStreams.get();
            for (Stream stream : streamList) {
                stream.setStatus(StreamStatus.ENDED);
                stream.setUpdatedAt(currentTime);
//                System.out.println("token : " + token.getToken() + ", userId : " + token.getUser().getEmail() + ", createdTime : " + token.getCreatedTime());
            }
            streamRepository.saveAll(streamList);
        }

        Stream stream = Stream.builder()
                .status(StreamStatus.PAUSED)
                .user(user)
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .build();

        stream = streamRepository.save(stream);

        return uuidToBase64(String.valueOf(stream.getId()));
    }

    public String getStreamKey() {
        String userId = securityService.getUserInfo().getUserId();
        Optional<List<Stream>> stream = streamRepository.findByUserIdAndStatus(UUID.fromString(userId), StreamStatus.PAUSED);
        if (stream.isEmpty() || stream.get().isEmpty()) {
            throw new IllegalArgumentException("No stream found by such key");
        }
        List<Stream> streamList = stream.get();
        return uuidToBase64(String.valueOf(streamList.get(0).getId()));
    }

    public StreamResponse verifyKey(String streamKey) {
        String streamUid = uuidFromBase64(streamKey);

        Optional<Stream> stream = streamRepository.findById(UUID.fromString(streamUid));
        if (stream.isEmpty()) {
            throw new IllegalArgumentException("No such stream key exist");
        }
        Stream stream1 = stream.get();
        return StreamResponse.builder()
                .createdAt(stream1.getCreatedAt())
                .updatedAt(stream1.getUpdatedAt())
                .status(stream1.getStatus())
                .user(stream1.getUser())
                .streamKey(String.valueOf(stream1.getId()))
                .build();

    }

    public Video onPublish(String streamKey) {
        String streamUid = uuidFromBase64(streamKey);

        Optional<Stream> stream = streamRepository.findById(UUID.fromString(streamUid));
        if (stream.isEmpty()) {
            throw new IllegalArgumentException("No such stream key exist");
        }
        Stream stream1 = stream.get();
        Instant currentTIme = Instant.now();
        Video video = Video.builder()
                .createdAt(currentTIme)
                .updatedAt(currentTIme)
                .stream(stream1)
                .videoStatus(VideoStatus.LIVE)
                .videoUrl(streamUid + "/" + currentTIme.toString())
                .build();

        Video video1 = videoRepository.save(video);
        stream1.setVideo(video1);

        streamRepository.save(stream1);
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
        Video video = stream1.getVideo();
        video.setUpdatedAt(currentTIme);
        video.setVideoStatus(VideoStatus.ENDED);
        videoRepository.save(video);
        return video;
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
