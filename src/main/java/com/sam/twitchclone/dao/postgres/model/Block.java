package com.sam.twitchclone.dao.postgres.model;

import com.sam.twitchclone.dao.postgres.model.user.User;
import jakarta.persistence.*;
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
@Table(indexes = {
        @Index(columnList = "src_blocker_id"),
        @Index(columnList = "end_blocker_id"),
        @Index(name = "uniqueIndex", columnList = "src_blocker_id, end_blocker_id", unique = true)
})
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "src_blocker_id")
    private User srcBlocker; //the person who is blocking

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_blocker_id")
    private User endBlocker; //the person who is being blocked

    private Instant createdAt;
    private Instant updatedAt;
}
