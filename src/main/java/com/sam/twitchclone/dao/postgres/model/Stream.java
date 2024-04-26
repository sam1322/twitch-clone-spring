package com.sam.twitchclone.dao.postgres.model;

import com.sam.twitchclone.dao.postgres.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
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

//    @NotNull
//    @Enumerated(EnumType.STRING)
//    private StreamStatus status;

    private boolean live;
    private boolean expired;
    private boolean chatEnabled;
    private boolean chatDelayed;
    private boolean chatFollowerOnly;

//    @OneToOne( cascade = CascadeType.REMOVE)
////    @ManyToOne( cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "user_id")
//    @NotNull
//    private User user;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @OneToMany(mappedBy = "stream", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC") // Order by the createdAt field in descending order
    private List<Video> videos;

    public Video getLatestVideo() {
        return !videos.isEmpty() ? videos.get(0) : null;
    }


//    @OneToOne
//    @MapsId
//    @JoinColumn(name = "user_id")
//    private User user;

//    @OneToOne(cascade = CascadeType.REMOVE,orphanRemoval = true)
//    @JoinColumn(name = "video_id")
//    private Video video;

  /*  @OneToMany(mappedBy = "id",cascade = CascadeType.REMOVE,orphanRemoval = true)
    @JoinColumn(name = "video_id")
    private List<Video> video;*/
}
