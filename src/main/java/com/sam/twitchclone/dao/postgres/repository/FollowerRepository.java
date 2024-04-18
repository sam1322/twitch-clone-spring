package com.sam.twitchclone.dao.postgres.repository;

import com.sam.twitchclone.dao.postgres.model.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowerRepository extends JpaRepository<Follower, UUID> {
    List<Follower> findBySrcFollowerId(UUID followerId);
    List<Follower> findByEndFollowerId(UUID followerId);

    Optional<Follower> findBySrcFollowerIdAndEndFollowerId(UUID srcFollowerId,UUID endFollowerId);

}
