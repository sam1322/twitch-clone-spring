package com.sam.twitchclone.dao.postgres.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sam.twitchclone.dao.postgres.model.Block;
import com.sam.twitchclone.dao.postgres.model.Follower;
import com.sam.twitchclone.dao.postgres.model.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String fullName;
    private String userImage;

    private String email;
    private String password;
    private String bio;
    private boolean verified;


    //    private ZonedDateTime createdTime;
//    private ZonedDateTime updatedTime;
    private Instant createdTime;
    private Instant updatedTime;


    @Enumerated(value = EnumType.STRING)
    Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Token> tokens;

    @OneToMany(mappedBy = "srcFollower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follower> srcFollowers = new HashSet<>();

    @OneToMany(mappedBy = "endFollower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follower> endFollowers = new HashSet<>();


    @OneToMany(mappedBy = "srcBlocker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Block> srcblockeders = new HashSet<>();

    @OneToMany(mappedBy = "endBlocker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Block> endBlockers = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return String.valueOf(id);
//        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
