package com.sam.twitchclone.dao.postgres.model;

import com.sam.twitchclone.constant.enums.StreamStatus;
import com.sam.twitchclone.dao.postgres.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stream {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StreamStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @OneToOne
    @JoinColumn(name = "video_id")
    private Video video;


}
