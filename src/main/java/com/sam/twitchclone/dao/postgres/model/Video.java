package com.sam.twitchclone.dao.postgres.model;

import com.sam.twitchclone.constant.enums.VideoStatus;
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
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private Instant createdAt;
    @NotNull
    private Instant updatedAt;


    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "stream_id")
    @NotNull
    private Stream stream;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VideoStatus videoStatus;

    private double videoSize;

    @NotNull
    private String videoUrl;


}
