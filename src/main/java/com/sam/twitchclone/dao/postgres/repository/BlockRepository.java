package com.sam.twitchclone.dao.postgres.repository;

import com.sam.twitchclone.dao.postgres.model.Block;
import com.sam.twitchclone.dao.postgres.model.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlockRepository extends JpaRepository<Block, UUID> {

    List<Block> findBySrcBlockerId(UUID srcBlockerId);
//    List<Follower> findByEndFollowerId(UUID followerId);
    Optional<Block> findBySrcBlockerIdAndEndBlockerId(UUID srcFollowerId, UUID endFollowerId);
}
