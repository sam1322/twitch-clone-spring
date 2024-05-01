package com.sam.twitchclone.dao.postgres;

import com.sam.twitchclone.dao.postgres.model.Video;
import com.sam.twitchclone.dao.postgres.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private Instant createdAt;
    @NotNull
    private Instant updatedAt;

    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "video_id")
    @NotNull
    private Video video;

}
