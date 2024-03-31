package com.sam.twitchclone.dao.postgres.model;

import com.sam.twitchclone.dao.postgres.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "token")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String token;

    @Column(name = "is_logged_out")
    private boolean loggedOut;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

//    private ZonedDateTime createdTime;
//    private ZonedDateTime updatedTime;

    private Instant createdTime;
    private Instant updatedTime;
}
