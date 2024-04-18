package com.sam.twitchclone.dao.postgres.model;

import com.sam.twitchclone.dao.postgres.model.user.User;
import jakarta.persistence.*;
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
@Table(indexes = {
        @Index(columnList = "src_follower_id"),
        @Index(columnList = "end_follower_id"),
        @Index(name = "uniqueIndex", columnList = "src_follower_id, end_follower_id", unique = true)
})
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @ManyToOne
    @JoinColumn(name = "src_follower_id")
    private User srcFollower; // the user which is following the end follower

    @ManyToOne(fetch = FetchType.LAZY)
//    @ManyToOne
    @JoinColumn(name = "end_follower_id")
    private User endFollower; // the user which has been followed by the src follower

     private Instant createdAt;
     private Instant updatedAt;
}
